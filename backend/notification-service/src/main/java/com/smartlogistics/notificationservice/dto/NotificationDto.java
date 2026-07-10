package com.smartlogistics.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationDto {

    public record CreateRequest(
            @NotNull(message = "Recipient ID is required")
            UUID recipientId,
            
            @NotBlank(message = "Notification type is required")
            String type, // MARKETING, SYSTEM, TRIP_UPDATE, SHIPMENT_UPDATE
            
            @NotBlank(message = "Notification channel is required")
            String channel, // EMAIL, SMS, PUSH, IN_APP, WHATSAPP
            
            @NotBlank(message = "Notification title is required")
            String title,
            
            @NotBlank(message = "Notification message is required")
            String message
    ) {}

    public record Response(
            UUID id,
            UUID recipientId,
            String type,
            String channel,
            String title,
            String message,
            String status,
            boolean isRead,
            LocalDateTime readAt,
            String failureReason,
            Integer retryCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
