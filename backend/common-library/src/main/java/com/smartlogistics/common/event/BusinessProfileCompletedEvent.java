package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record BusinessProfileCompletedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID userId,
        String companyName,
        String taxId
) {}
