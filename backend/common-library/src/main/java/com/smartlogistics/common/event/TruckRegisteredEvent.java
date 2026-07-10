package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record TruckRegisteredEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID truckId,
        UUID ownerId,
        String licensePlate,
        String type,
        Double capacity
) {}
