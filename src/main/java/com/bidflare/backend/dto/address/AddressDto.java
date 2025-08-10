package com.bidflare.backend.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class AddressDto {
    public record AddressRequest(
            @NotBlank(message = "Label is required")
            @Size(max = 50, message = "Label cannot exceed 50 characters")
            String label,

            @NotBlank(message = "Address line 1 is required")
            @Size(max = 255)
            String addressLine1,

            @Size(max = 255)
            String addressLine2,

            @NotBlank(message = "City is required")
            @Size(max = 100)
            String city,

            @NotBlank(message = "State/Province is required")
            @Size(max = 100)
            String stateProvince,

            @NotBlank(message = "Postal code is required")
            @Size(max = 20)
            String postalCode,

            @NotBlank(message = "Country is required")
            @Size(max = 100)
            String country,

            @NotNull
            boolean isDefault
    ) {}
    public record AddressResponse(
            UUID id,
            String label,
            String addressLine1,
            String addressLine2,
            String city,
            String stateProvince,
            String postalCode,
            String country,
            boolean isDefault,
            Instant createdAt,
            Instant updatedAt
    ) {}
}