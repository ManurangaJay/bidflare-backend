package com.bidflare.backend.mapper;

import com.bidflare.backend.dto.notification.NotificationCreateDto;
import com.bidflare.backend.dto.notification.NotificationResponseDto;
import com.bidflare.backend.entity.Notification;
import com.bidflare.backend.entity.User;

import java.time.LocalDateTime;

public class NotificationMapper {

    public static Notification toEntity(NotificationCreateDto dto, User user) {
        return Notification.builder()
                .user(user)
                .type(dto.type())
                .message(dto.message())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static NotificationResponseDto toDto(Notification n) {
        return new NotificationResponseDto(
                n.getId(),
                n.getUser().getId(),
                n.getType(),
                n.getMessage(),
                n.getIsRead(),
                n.getCreatedAt()
        );
    }
}
