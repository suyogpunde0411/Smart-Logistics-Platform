package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentUpdatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID shipmentId,
        String origin,
        String destination,
        Double weight,
        String status
) {}
