package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record BidCreatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID bidId,
        UUID shipmentId,
        UUID driverId,
        Double amount
) {}
