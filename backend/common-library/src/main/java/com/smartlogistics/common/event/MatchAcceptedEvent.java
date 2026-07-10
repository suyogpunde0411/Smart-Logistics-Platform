package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record MatchAcceptedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID matchId,
        UUID shipmentId,
        UUID driverId,
        UUID truckId,
        Double price
) {}
