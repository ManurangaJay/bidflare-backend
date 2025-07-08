package com.bidflare.backend.dto.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String title,
        String description,
        BigDecimal startingPrice,
        String status,
        UUID sellerId,
        UUID categoryId,
        Instant createdAt,
        Instant updatedAt
) {}
