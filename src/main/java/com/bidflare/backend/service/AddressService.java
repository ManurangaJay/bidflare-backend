package com.bidflare.backend.service;

import com.bidflare.backend.dto.address.AddressDto;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressDto.AddressResponse createAddress(AddressDto.AddressRequest requestDto, UUID userId);
    List<AddressDto.AddressResponse> getAddressesByUserId(UUID userId);
    AddressDto.AddressResponse updateAddress(UUID addressId, AddressDto.AddressRequest requestDto, UUID userId);
    AddressDto.AddressResponse setDefaultAddress(UUID addressId, UUID userId);
    void deleteAddress(UUID addressId, UUID userId);
}