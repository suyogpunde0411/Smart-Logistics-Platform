package com.smartlogistics.trackingservice.events;

import java.time.Instant;
import java.util.UUID;

public record TripCreatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID tripId,
        UUID shipmentId,
        UUID truckId,
        UUID driverId,
        UUID businessId
) {}
