package com.smartlogistics.truckservice.events;

import java.time.Instant;
import java.util.UUID;

public record TruckInsuranceExpiredEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID truckId,
        String policyNumber,
        String expiryDate
) {}
