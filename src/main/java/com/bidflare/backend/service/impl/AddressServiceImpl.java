package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.address.AddressDto;
import com.bidflare.backend.entity.Address;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.exception.ResourceNotFoundException;
import com.bidflare.backend.mapper.AddressMapper;
import com.bidflare.backend.repository.AddressRepository;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressDto.AddressResponse createAddress(AddressDto.AddressRequest requestDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Address address = addressMapper.toEntity(requestDto);
        address.setUser(user);

        if (address.isDefault()) {
            handleSetDefault(userId, null);
        }

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDto.AddressResponse> getAddressesByUserId(UUID userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto.AddressResponse updateAddress(UUID addressId, AddressDto.AddressRequest requestDto, UUID userId) {
        Address address = findAddressForUser(addressId, userId);

        if (requestDto.isDefault() && !address.isDefault()) {
            handleSetDefault(userId, null);
        }

        addressMapper.updateEntityFromDto(requestDto, address);
        Address updatedAddress = addressRepository.save(address);
        return addressMapper.toDto(updatedAddress);
    }

    @Override
    @Transactional
    public AddressDto.AddressResponse setDefaultAddress(UUID addressId, UUID userId) {
        Address newDefaultAddress = findAddressForUser(addressId, userId);

        if (!newDefaultAddress.isDefault()) {
            handleSetDefault(userId, addressId);
            newDefaultAddress.setDefault(true);
            addressRepository.save(newDefaultAddress);
        }

        return addressMapper.toDto(newDefaultAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(UUID addressId, UUID userId) {
        Address address = findAddressForUser(addressId, userId);
        addressRepository.delete(address);
    }

    private Address findAddressForUser(UUID addressId, UUID userId) {
        return addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressId + " for this user."));
    }

    private void handleSetDefault(UUID userId, UUID newDefaultAddressId) {
        addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .ifPresent(oldDefault -> {
                    if (newDefaultAddressId == null || !oldDefault.getId().equals(newDefaultAddressId)) {
                        oldDefault.setDefault(false);
                        addressRepository.save(oldDefault);
                    }
                });
    }
}