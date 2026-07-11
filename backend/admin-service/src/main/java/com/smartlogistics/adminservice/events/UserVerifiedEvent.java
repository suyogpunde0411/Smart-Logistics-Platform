package com.smartlogistics.adminservice.events;

import java.time.Instant;
import java.util.UUID;

public record UserVerifiedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID userId,
        String status,
        String comments
) {}
