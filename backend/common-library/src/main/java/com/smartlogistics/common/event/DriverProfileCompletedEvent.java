package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record DriverProfileCompletedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID userId,
        String licenseNumber,
        Integer experienceYears
) {}
