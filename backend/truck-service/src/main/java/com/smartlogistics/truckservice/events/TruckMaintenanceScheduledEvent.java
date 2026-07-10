package com.smartlogistics.truckservice.events;

import java.time.Instant;
import java.util.UUID;

public record TruckMaintenanceScheduledEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID truckId,
        UUID maintenanceId,
        String maintenanceDate,
        Double cost
) {}
