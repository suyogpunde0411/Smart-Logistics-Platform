package com.smartlogistics.trackingservice.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShipmentStatusChangedEvent(
        UUID eventId,
        LocalDateTime timestamp,
        String correlationId,
        UUID shipmentId,
        UUID businessOwnerId,
        String oldStatus,
        String newStatus,
        String remarks
) {}
