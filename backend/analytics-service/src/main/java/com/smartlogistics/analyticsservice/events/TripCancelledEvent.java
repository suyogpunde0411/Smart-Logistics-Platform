package com.smartlogistics.analyticsservice.events;

import java.time.Instant;
import java.util.UUID;

public record TripCancelledEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID tripId,
        String reason
) {}
