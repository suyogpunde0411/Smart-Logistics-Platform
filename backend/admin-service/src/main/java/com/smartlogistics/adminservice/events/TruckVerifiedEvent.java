package com.smartlogistics.adminservice.events;

import java.time.Instant;
import java.util.UUID;

public record TruckVerifiedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID truckId,
        String status,
        String comments
) {}
