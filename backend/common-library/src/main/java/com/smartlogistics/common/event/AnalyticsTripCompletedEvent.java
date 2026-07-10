package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record AnalyticsTripCompletedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID tripId,
        UUID driverId,
        Double distance,
        Double durationMinutes,
        Double revenue
) {}
