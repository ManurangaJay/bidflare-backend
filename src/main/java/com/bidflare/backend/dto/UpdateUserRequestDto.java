package com.bidflare.backend.dto;

import lombok.Builder;

@Builder
public record UpdateUserRequestDto(
        String name,
        String email,
        String password,
        String profileImage
) {}
