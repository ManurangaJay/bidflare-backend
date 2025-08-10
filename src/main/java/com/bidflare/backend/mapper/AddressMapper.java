package com.bidflare.backend.mapper;

import com.bidflare.backend.dto.address.AddressDto;
import com.bidflare.backend.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDto.AddressResponse toDto(Address address) {
        if (address == null) return null;
        return new AddressDto.AddressResponse(
                address.getId(),
                address.getLabel(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getStateProvince(),
                address.getPostalCode(),
                address.getCountry(),
                address.isDefault(),
                address.getCreatedAt(),
                address.getUpdatedAt()
        );
    }

    public Address toEntity(AddressDto.AddressRequest dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setLabel(dto.label());
        address.setAddressLine1(dto.addressLine1());
        address.setAddressLine2(dto.addressLine2());
        address.setCity(dto.city());
        address.setStateProvince(dto.stateProvince());
        address.setPostalCode(dto.postalCode());
        address.setCountry(dto.country());
        address.setDefault(dto.isDefault());
        return address;
    }

    public void updateEntityFromDto(AddressDto.AddressRequest dto, Address address) {
        if (dto == null || address == null) return;
        address.setLabel(dto.label());
        address.setAddressLine1(dto.addressLine1());
        address.setAddressLine2(dto.addressLine2());
        address.setCity(dto.city());
        address.setStateProvince(dto.stateProvince());
        address.setPostalCode(dto.postalCode());
        address.setCountry(dto.country());
        address.setDefault(dto.isDefault());
    }
}