package com.bidflare.backend.dto.notification;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponseDto(
        UUID id,
        UUID userId,
        String type,
        String message,
        Boolean isRead,
        LocalDateTime createdAt
) {}
