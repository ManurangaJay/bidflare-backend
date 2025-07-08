package com.bidflare.backend.dto.product;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductRequestDto(
        String title,
        String description,
        BigDecimal startingPrice,
        UUID categoryId,
        String status
) {}
