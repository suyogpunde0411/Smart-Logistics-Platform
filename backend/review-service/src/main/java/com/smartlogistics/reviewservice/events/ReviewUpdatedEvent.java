package com.smartlogistics.reviewservice.events;

import java.time.Instant;
import java.util.UUID;

public record ReviewUpdatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID reviewId,
        UUID revieweeId,
        Integer rating,
        String comment
) {}
