package com.smartlogistics.analyticsservice.events;

import java.time.Instant;
import java.util.UUID;

public record TrustScoreUpdatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID userId,
        Integer oldScore,
        Integer newScore,
        String reason
) {}
