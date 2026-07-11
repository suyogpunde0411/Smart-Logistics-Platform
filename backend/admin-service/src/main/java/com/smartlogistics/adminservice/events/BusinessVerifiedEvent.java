package com.smartlogistics.adminservice.events;

import java.time.Instant;
import java.util.UUID;

public record BusinessVerifiedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID businessId,
        String status,
        String comments
) {}
