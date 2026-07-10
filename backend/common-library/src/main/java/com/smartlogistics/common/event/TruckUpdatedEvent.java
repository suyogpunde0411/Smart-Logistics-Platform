package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record TruckUpdatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID truckId,
        String licensePlate,
        String type,
        Double capacity
) {}
