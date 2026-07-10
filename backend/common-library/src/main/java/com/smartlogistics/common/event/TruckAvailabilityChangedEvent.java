

package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record TruckAvailabilityChangedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID truckId,
        String status,
        Double currentLatitude,
        Double currentLongitude
) {}
