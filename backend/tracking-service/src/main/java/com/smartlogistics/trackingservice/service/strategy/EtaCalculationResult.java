package com.smartlogistics.trackingservice.service.strategy;

import java.time.LocalDateTime;

public record EtaCalculationResult(
        LocalDateTime expectedArrival,
        double remainingDistanceKm,
        double remainingTimeHours,
        double averageSpeedKmh
) {}
