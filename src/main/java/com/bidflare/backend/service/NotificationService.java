package com.bidflare.backend.service;

import com.bidflare.backend.dto.notification.NotificationCreateDto;
import com.bidflare.backend.dto.notification.NotificationResponseDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationResponseDto create(NotificationCreateDto dto);
    List<NotificationResponseDto> getByUser(UUID userId);
    NotificationResponseDto markAsRead(UUID id);
    void delete(UUID id);
}
