package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record TripCompletedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID tripId,
        UUID shipmentId,
        UUID driverId,
        UUID truckId,
        Double finalDistance,
        Instant endTimestamp
) {}
