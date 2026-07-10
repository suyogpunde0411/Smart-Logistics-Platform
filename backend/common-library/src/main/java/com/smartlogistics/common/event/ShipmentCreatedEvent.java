package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentCreatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID shipmentId,
        UUID businessId,
        String origin,
        String destination,
        Double weight,
        String specialRequirements,
        Double budget
) {}
