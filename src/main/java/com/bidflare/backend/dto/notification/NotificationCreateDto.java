package com.bidflare.backend.dto.notification;

import java.util.UUID;

public record NotificationCreateDto(
        UUID userId,
        String type,
        String message
) {}
