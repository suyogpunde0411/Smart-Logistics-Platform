package com.smartlogistics.trackingservice.service.strategy;

import com.smartlogistics.trackingservice.entity.GpsLocation;
import com.smartlogistics.trackingservice.entity.Trip;

import java.time.Duration;
import java.time.LocalDateTime;

public class AverageSpeedEtaStrategy implements EtaCalculationStrategy {

    @Override
    public EtaCalculationResult calculateEta(Trip trip, GpsLocation currentLocation, double remainingDistanceKm, double defaultSpeedKmh) {
        double averageSpeed = defaultSpeedKmh;

        if (trip.getStartedAt() != null) {
            double elapsedHours = Duration.between(trip.getStartedAt(), LocalDateTime.now()).toMinutes() / 60.0;
            double distanceTravelled = trip.getTotalDistanceTravelledKm() != null ? trip.getTotalDistanceTravelledKm() : 0.0;

            if (elapsedHours > 0.05 && distanceTravelled > 0.1) {
                double computedSpeed = distanceTravelled / elapsedHours;
                if (computedSpeed >= 5.0 && computedSpeed <= 150.0) {
                    averageSpeed = computedSpeed;
                }
            }
        }

        double remainingTimeHours = remainingDistanceKm / averageSpeed;
        long remainingMinutes = (long) (remainingTimeHours * 60.0);
        LocalDateTime expectedArrival = LocalDateTime.now().plusMinutes(remainingMinutes);

        return new EtaCalculationResult(expectedArrival, remainingDistanceKm, remainingTimeHours, averageSpeed);
    }
}
