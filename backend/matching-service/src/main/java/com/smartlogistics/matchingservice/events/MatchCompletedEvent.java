package com.smartlogistics.matchingservice.events;

import java.time.Instant;
import java.util.UUID;

public record MatchCompletedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID matchId,
        UUID shipmentId,
        UUID truckId,
        UUID driverId
) {}
