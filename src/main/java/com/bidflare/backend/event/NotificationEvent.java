package com.bidflare.backend.event;

import lombok.Getter;
import java.util.List;
import java.util.UUID;

@Getter
public class NotificationEvent {
    private final List<UUID> recipientIds;
    private final String type;
    private final String message;
    private final UUID referenceId;

    // Constructor for Multiple Users
    public NotificationEvent(List<UUID> recipientIds, String type, String message, UUID referenceId) {
        this.recipientIds = recipientIds;
        this.type = type;
        this.message = message;
        this.referenceId = referenceId;
    }

    public NotificationEvent(UUID userId, String type, String message, UUID referenceId) {
        this.recipientIds = List.of(userId);
        this.type = type;
        this.message = message;
        this.referenceId = referenceId;
    }
}