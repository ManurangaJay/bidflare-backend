package com.bidflare.backend.dto.review;

import java.util.UUID;

public record ReviewCreateDto(
        UUID reviewerId,
        UUID sellerId,
        int rating,
        String comment
) {}
