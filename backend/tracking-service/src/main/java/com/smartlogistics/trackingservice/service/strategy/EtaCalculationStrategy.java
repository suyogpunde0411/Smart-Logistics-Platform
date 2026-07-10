package com.smartlogistics.trackingservice.service.strategy;

import com.smartlogistics.trackingservice.entity.GpsLocation;
import com.smartlogistics.trackingservice.entity.Trip;

public interface EtaCalculationStrategy {
    EtaCalculationResult calculateEta(Trip trip, GpsLocation currentLocation, double remainingDistanceKm, double defaultSpeedKmh);
}
