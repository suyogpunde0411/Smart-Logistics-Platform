package com.smartlogistics.trackingservice.service.strategy;

import com.smartlogistics.trackingservice.entity.GpsLocation;
import com.smartlogistics.trackingservice.entity.Trip;

import java.time.LocalDateTime;

public class DefaultTrafficEtaStrategy implements EtaCalculationStrategy {

    private static final double TRAFFIC_BUFFER_MULTIPLIER = 1.15; // 15% delay buffer

    @Override
    public EtaCalculationResult calculateEta(Trip trip, GpsLocation currentLocation, double remainingDistanceKm, double defaultSpeedKmh) {
        double averageSpeed = defaultSpeedKmh;
        double baseTimeHours = remainingDistanceKm / averageSpeed;
        double remainingTimeHours = baseTimeHours * TRAFFIC_BUFFER_MULTIPLIER;

        long remainingMinutes = (long) (remainingTimeHours * 60.0);
        LocalDateTime expectedArrival = LocalDateTime.now().plusMinutes(remainingMinutes);

        return new EtaCalculationResult(expectedArrival, remainingDistanceKm, remainingTimeHours, averageSpeed);
    }
}
