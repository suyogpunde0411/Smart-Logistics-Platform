package com.smartlogistics.reviewservice.events;

import java.time.Instant;
import java.util.UUID;

public record UserDeletedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID userId
) {}
