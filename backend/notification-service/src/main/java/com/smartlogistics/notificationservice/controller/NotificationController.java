package com.smartlogistics.notificationservice.controller;

import com.smartlogistics.notificationservice.dto.NotificationDto;
import com.smartlogistics.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "Endpoints for user alerts history, status reading, and in-app dispatches")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Search and filter notifications history")
    public ResponseEntity<Page<NotificationDto.Response>> searchNotifications(
            @RequestParam(required = false) UUID recipientId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String channel,
            @RequestParam(required = false) Boolean unread,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {

        UUID targetRecipient = recipientId;
        String loggedUserStr = getLoggedUserId();
        boolean isAdmin = isAdmin();

        if (!isAdmin) {
            // Enforce users can only see their own notifications
            targetRecipient = UUID.fromString(loggedUserStr);
        } else if (targetRecipient == null) {
            targetRecipient = UUID.fromString(loggedUserStr);
        }

        return ResponseEntity.ok(notificationService.searchNotifications(
                targetRecipient, type, status, channel, unread, startDate, endDate, pageable));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
        UUID recipientId = UUID.fromString(getLoggedUserId());
        notificationService.markAsRead(id, recipientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Soft delete a notification")
    public ResponseEntity<Void> deleteNotification(@PathVariable UUID id) {
        UUID recipientId = UUID.fromString(getLoggedUserId());
        notificationService.deleteNotification(id, recipientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unread-count")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get unread count of notifications")
    public ResponseEntity<Long> getUnreadCount() {
        UUID recipientId = UUID.fromString(getLoggedUserId());
        return ResponseEntity.ok(notificationService.getUnreadCount(recipientId));
    }

    @PostMapping("/in-app")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Manually push an In-App alert")
    public ResponseEntity<NotificationDto.Response> createInAppNotification(
            @Valid @RequestBody NotificationDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createInAppNotification(request));
    }

    private String getLoggedUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
