package com.smartlogistics.notificationservice.events;

import java.time.Instant;
import java.util.UUID;

public record NotificationSentEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID notificationId,
        UUID recipientId,
        String channel,
        String type
) {}
