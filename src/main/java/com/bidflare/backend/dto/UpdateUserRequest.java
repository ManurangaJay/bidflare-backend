package com.bidflare.backend.dto;

import lombok.Builder;
import java.util.UUID;

@Builder
public record UpdateUserRequest(
        String name,
        String email,
        String password,
        String profileImage
) {}
