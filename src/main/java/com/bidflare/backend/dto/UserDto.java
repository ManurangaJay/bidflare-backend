package com.bidflare.backend.dto;

import com.bidflare.backend.entity.User.Role;
import lombok.Builder;

import java.util.UUID;


@Builder
public record UserDto(
        UUID id,
        String email,
        String name,
        Role role,
        boolean isVerified,
        String profileImage
) {}
