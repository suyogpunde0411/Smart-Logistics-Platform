package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.entity.GpsLocation;
import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.service.strategy.AverageSpeedEtaStrategy;
import com.smartlogistics.trackingservice.service.strategy.DefaultTrafficEtaStrategy;
import com.smartlogistics.trackingservice.service.strategy.EtaCalculationResult;
import com.smartlogistics.trackingservice.service.strategy.EtaCalculationStrategy;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EtaStrategyTest {

    @Test
    public void testDefaultTrafficEtaStrategy() {
        EtaCalculationStrategy strategy = new DefaultTrafficEtaStrategy();
        Trip trip = new Trip();
        GpsLocation loc = new GpsLocation();

        // 100 km distance at 50 km/h default speed = 2.0 hours base.
        // With 15% traffic buffer = 2.3 hours remaining. Expected minutes = 2.3 * 60 = 138 minutes.
        EtaCalculationResult result = strategy.calculateEta(trip, loc, 100.0, 50.0);

        assertNotNull(result.expectedArrival());
        assertEquals(100.0, result.remainingDistanceKm());
        assertEquals(50.0, result.averageSpeedKmh());
        assertEquals(2.3, result.remainingTimeHours(), 0.01);
        assertTrue(result.expectedArrival().isAfter(LocalDateTime.now()));
    }

    @Test
    public void testAverageSpeedEtaStrategy() {
        EtaCalculationStrategy strategy = new AverageSpeedEtaStrategy();
        Trip trip = new Trip();

        // Set up trip started 2 hours ago and travelled 120 km
        LocalDateTime startedAt = LocalDateTime.now().minusHours(2);
        trip.setStartedAt(startedAt);
        trip.setTotalDistanceTravelledKm(120.0);

        GpsLocation loc = new GpsLocation();

        // Travelled 120 km in 2 hours -> Average speed = 60 km/h.
        // Remaining distance = 180 km.
        // Remaining hours = 180 / 60 = 3.0 hours.
        EtaCalculationResult result = strategy.calculateEta(trip, loc, 180.0, 50.0);

        assertNotNull(result.expectedArrival());
        assertEquals(180.0, result.remainingDistanceKm());
        assertEquals(60.0, result.averageSpeedKmh(), 0.1);
        assertEquals(3.0, result.remainingTimeHours(), 0.1);
        assertTrue(result.expectedArrival().isAfter(LocalDateTime.now().plusHours(2).plusMinutes(55)));
    }
}
