package com.smartlogistics.trackingservice.events;

import java.time.Instant;
import java.util.UUID;

public record TripResumedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID tripId
) {}
