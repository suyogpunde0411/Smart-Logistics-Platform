package com.smartlogistics.reviewservice.events;

import java.time.Instant;
import java.util.UUID;

public record ReviewReportedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID reviewId,
        UUID reporterId,
        String reason
) {}
