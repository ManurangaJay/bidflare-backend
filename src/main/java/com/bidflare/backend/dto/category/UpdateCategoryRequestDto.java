package com.bidflare.backend.dto.category;

public record UpdateCategoryRequestDto(
        String name,
        String slug
) {}
