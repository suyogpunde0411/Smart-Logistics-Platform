package com.smartlogistics.trackingservice.events;

import java.time.Instant;
import java.util.UUID;

public record GpsUpdatedEvent(
        String eventId,
        Instant timestamp,
        String correlationId,
        UUID tripId,
        Double latitude,
        Double longitude,
        Double speedKmh,
        Double heading,
        Double distanceTravelledKm
) {}
