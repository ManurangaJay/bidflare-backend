package com.bidflare.backend.dto.address;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class AddressResponseDto {
    private UUID id;
    private String label;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private boolean isDefault;
    private Instant createdAt;
    private Instant updatedAt;
}