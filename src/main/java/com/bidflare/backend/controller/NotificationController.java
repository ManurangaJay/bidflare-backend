package com.bidflare.backend.controller;

import com.bidflare.backend.dto.notification.NotificationCreateDto;
import com.bidflare.backend.dto.notification.NotificationResponseDto;
import com.bidflare.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDto>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getByUser(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER')")
    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDto> markAsRead(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
