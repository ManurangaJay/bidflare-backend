package com.bidflare.backend.dto.review;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ReviewCreateDto(
        @NotNull UUID sellerId,
        @NotNull UUID productId,
        int rating,
        String comment
) {}
