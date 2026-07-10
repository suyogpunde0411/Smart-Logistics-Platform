package com.smartlogistics.common.event;

import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID userId,
        String email,
        String phone,
        String role,
        String status
) {}
