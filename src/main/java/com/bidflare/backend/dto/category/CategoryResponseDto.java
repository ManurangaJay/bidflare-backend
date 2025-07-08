package com.bidflare.backend.dto.category;

import java.util.UUID;

public record CategoryResponseDto(
        UUID id,
        String name,
        String slug
) {}
