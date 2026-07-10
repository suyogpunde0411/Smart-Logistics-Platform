package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record NotificationCreatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID notificationId,
        UUID recipientId,
        String type,
        String channel,
        String title,
        String message
) {}
