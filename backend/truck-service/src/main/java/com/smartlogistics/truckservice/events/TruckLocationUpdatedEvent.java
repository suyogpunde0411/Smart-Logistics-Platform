package com.smartlogistics.truckservice.events;

import java.time.Instant;
import java.util.UUID;

public record TruckLocationUpdatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID truckId,
        Double latitude,
        Double longitude,
        Double speed,
        Double heading
) {}
