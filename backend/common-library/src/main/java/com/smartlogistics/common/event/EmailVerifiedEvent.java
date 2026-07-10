package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record EmailVerifiedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID userId,
        String email
) {}
