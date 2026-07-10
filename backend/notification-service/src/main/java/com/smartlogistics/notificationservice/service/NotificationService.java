package com.smartlogistics.notificationservice.service;

import com.smartlogistics.notificationservice.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NotificationService {

    void sendDirectNotification(UUID recipientId, String type, String channel, String title, String message);

    void sendEmail(UUID recipientId, String subject, String body);

    void sendSms(UUID recipientId, String message);

    void sendPush(UUID recipientId, String title, String body);

    NotificationDto.Response createInAppNotification(NotificationDto.CreateRequest request);

    Page<NotificationDto.Response> searchNotifications(
            UUID recipientId, String type, String status, String channel, Boolean unread,
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    void markAsRead(UUID id, UUID recipientId);

    void deleteNotification(UUID id, UUID recipientId);

    long getUnreadCount(UUID recipientId);
}
