package com.bidflare.backend.dto.review;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponseDto(
        UUID id,
        UUID reviewerId,
        UUID sellerId,
        int rating,
        String comment,
        LocalDateTime createdAt
) {}
