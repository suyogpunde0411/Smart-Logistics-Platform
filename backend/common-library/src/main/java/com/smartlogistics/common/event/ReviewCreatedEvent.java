package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record ReviewCreatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID reviewId,
        UUID tripId,
        UUID reviewerId,
        UUID revieweeId,
        Integer rating,
        String comment
) {}
