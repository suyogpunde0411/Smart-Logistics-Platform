package com.smartlogistics.matchingservice.events;

import java.time.Instant;
import java.util.UUID;

public record BidRejectedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID bidId,
        UUID shipmentId,
        UUID truckId,
        UUID driverId,
        String reason
) {}
