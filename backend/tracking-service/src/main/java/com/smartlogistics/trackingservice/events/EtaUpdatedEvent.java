package com.smartlogistics.trackingservice.events;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record EtaUpdatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID tripId,
        LocalDateTime expectedArrivalTime,
        Double remainingDistanceKm,
        Double remainingTimeHours
) {}
