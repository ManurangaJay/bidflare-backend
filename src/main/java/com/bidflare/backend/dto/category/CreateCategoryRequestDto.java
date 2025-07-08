package com.bidflare.backend.dto.category;

public record CreateCategoryRequestDto(
        String name,
        String slug
) {}
