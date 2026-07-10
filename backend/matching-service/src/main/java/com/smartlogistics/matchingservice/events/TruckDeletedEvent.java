package com.smartlogistics.matchingservice.events;

import java.time.Instant;
import java.util.UUID;

public record TruckDeletedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID truckId
) {}
