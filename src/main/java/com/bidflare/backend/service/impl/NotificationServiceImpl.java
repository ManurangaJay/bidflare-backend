package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.notification.NotificationCreateDto;
import com.bidflare.backend.dto.notification.NotificationResponseDto;
import com.bidflare.backend.entity.Notification;
import com.bidflare.backend.event.NotificationEvent;
import com.bidflare.backend.mapper.NotificationMapper;
import com.bidflare.backend.repository.NotificationRepository;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

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

    @Async
    @EventListener
    @Transactional
    public void handleNotificationEvent(NotificationEvent event) {

        // 1. Fetch all users involved (Optional: strictly required only if you need User entity validation)
        // If your Mapper works with ID only, you can skip fetching the full User entity,
        // but JPA relationships usually require the User object.
        List<User> recipients = userRepository.findAllById(event.getRecipientIds());

        // 2. Prepare Entities for Batch Save
        List<Notification> notificationsToSave = recipients.stream()
                .map(user -> {
                    Notification n = new Notification();
                    n.setUser(user);
                    n.setType(event.getType());
                    n.setMessage(event.getMessage());
                    n.setIsRead(false);
                    // n.setReferenceId(event.getReferenceId()); // If you have this column
                    return n;
                }).toList();

        // 3. Batch Save to DB (Much faster than saving inside a loop)
        List<Notification> savedNotifications = notificationRepository.saveAll(notificationsToSave);

        // 4. Fan-out: Push to WebSockets individually
        // We loop through the SAVED notifications to ensure we send the generated ID
        for (Notification n : savedNotifications) {
            NotificationResponseDto responseDto = NotificationMapper.toDto(n);

            messagingTemplate.convertAndSend(
                    "/topic/user/" + n.getUser().getId() + "/notifications",
                    responseDto
            );
        }
    }
}
