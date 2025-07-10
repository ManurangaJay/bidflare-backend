package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.notification.NotificationCreateDto;
import com.bidflare.backend.dto.notification.NotificationResponseDto;
import com.bidflare.backend.entity.Notification;
import com.bidflare.backend.mapper.NotificationMapper;
import com.bidflare.backend.repository.NotificationRepository;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public NotificationResponseDto create(NotificationCreateDto dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Notification n = NotificationMapper.toEntity(dto, user);
        return NotificationMapper.toDto(notificationRepository.save(n));
    }

    @Override
    public List<NotificationResponseDto> getByUser(UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationMapper::toDto)
                .toList();
    }

    @Override
    public NotificationResponseDto markAsRead(UUID id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setIsRead(true);
        return NotificationMapper.toDto(notificationRepository.save(n));
    }

    @Override
    public void delete(UUID id) {
        notificationRepository.deleteById(id);
    }
}
