package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.entity.*;
import com.smartlogistics.trackingservice.repository.GpsLocationRepository;
import com.smartlogistics.trackingservice.repository.TripRepository;
import com.smartlogistics.trackingservice.repository.TripTimelineRepository;
import com.smartlogistics.trackingservice.service.strategy.EtaCalculationResult;
import com.smartlogistics.trackingservice.service.strategy.EtaCalculationStrategy;
import com.smartlogistics.trackingservice.service.strategy.AverageSpeedEtaStrategy;
import com.smartlogistics.trackingservice.service.strategy.DefaultTrafficEtaStrategy;
import com.smartlogistics.trackingservice.events.TrackingKafkaProducer;
import com.smartlogistics.trackingservice.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripScheduler {

    private final TripRepository tripRepository;
    private final GpsLocationRepository gpsLocationRepository;
    private final TripTimelineRepository timelineRepository;
    private final TrackingKafkaProducer kafkaProducer;

    @Value("${tracking.eta.strategy:AVERAGE_SPEED}")
    private String etaStrategyName;

    @Value("${tracking.eta.default-speed-kmh:50.0}")
    private double defaultSpeedKmh;

    @Value("${tracking.eta.timeout-hours:24}")
    private int timeoutHours;

    @Value("${tracking.eta.inactivity-hours:4}")
    private int inactivityHours;

    @Value("${tracking.eta.history-cleanup-days:30}")
    private int cleanupDays;

    @Scheduled(fixedDelay = 120000) // Recalculate ETA every 2 minutes
    @Transactional
    public void recalculateActiveEtas() {
        log.info("Starting scheduled active ETAs recalculation...");
        List<Trip> activeTrips = tripRepository.findByStatusInAndIsDeletedFalse(
                List.of("STARTED", "IN_PROGRESS", "RESUMED")
        );

        for (Trip trip : activeTrips) {
            try {
                List<GpsLocation> locations = gpsLocationRepository.findByTripIdAndIsDeletedFalseOrderByTimestampAsc(trip.getId());
                GpsLocation currentLoc = locations.isEmpty() ? null : locations.get(locations.size() - 1);

                double lat = currentLoc != null ? currentLoc.getLatitude() : trip.getRoute().getStartLatitude();
                double lng = currentLoc != null ? currentLoc.getLongitude() : trip.getRoute().getStartLongitude();

                double endLat = trip.getRoute().getEndLatitude();
                double endLng = trip.getRoute().getEndLongitude();

                double remainingDistance = GeoUtils.calculateDistance(lat, lng, endLat, endLng);
                trip.setRemainingDistanceKm(remainingDistance);

                EtaCalculationStrategy strategy = getEtaStrategy(etaStrategyName);
                EtaCalculationResult result = strategy.calculateEta(trip, currentLoc, remainingDistance, defaultSpeedKmh);

                trip.setExpectedArrivalTime(result.expectedArrival());
                trip.setAverageSpeedKmh(result.averageSpeedKmh());
                tripRepository.save(trip);

                kafkaProducer.publishEtaUpdated(trip.getId(), result.expectedArrival(), remainingDistance, result.remainingTimeHours());
                log.debug("Recalculated ETA for Trip ID: {}. Expected arrival: {}", trip.getId(), result.expectedArrival());
            } catch (Exception e) {
                log.error("Failed to recalculate ETA for Trip ID: {}. Error: {}", trip.getId(), e.getMessage());
            }
        }
    }

    @Scheduled(fixedDelay = 300000) // Audit timeouts and inactivity every 5 minutes
    @Transactional
    public void detectTimeoutsAndInactivity() {
        log.info("Running scheduled active trip timeout and inactivity audit...");
        List<Trip> activeTrips = tripRepository.findByStatusInAndIsDeletedFalse(
                List.of("STARTED", "IN_PROGRESS", "RESUMED")
        );

        LocalDateTime now = LocalDateTime.now();

        for (Trip trip : activeTrips) {
            try {
                // 1. Timeout Alert
                if (trip.getStartedAt() != null) {
                    long activeHours = java.time.Duration.between(trip.getStartedAt(), now).toHours();
                    if (activeHours >= timeoutHours) {
                        createTimelineEvent(trip.getId(), "DELAYED", "Trip Timeout Warning",
                                "Trip execution has exceeded the standard active threshold of " + timeoutHours + " hours.");
                        log.warn("Trip ID: {} has exceeded active duration timeout", trip.getId());
                    }
                }

                // 2. Inactivity Alert
                LocalDateTime lastUpdate = trip.getLastGpsUpdatedAt() != null ? trip.getLastGpsUpdatedAt() : trip.getStartedAt();
                if (lastUpdate != null) {
                    long silentHours = java.time.Duration.between(lastUpdate, now).toHours();
                    if (silentHours >= inactivityHours) {
                        createTimelineEvent(trip.getId(), "DELAYED", "Trip Inactivity Alert",
                                "No GPS telemetry received for over " + inactivityHours + " hours.");
                        log.warn("Trip ID: {} matches inactivity threshold (silent for {} hours)", trip.getId(), silentHours);
                    }
                }
            } catch (Exception e) {
                log.error("Error auditing Trip ID: {}. Error: {}", trip.getId(), e.getMessage());
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Clean up old GPS history daily at midnight
    @Transactional
    public void cleanExpiredTripTelemetry() {
        log.info("Running telemetry logs database retention archive...");
        LocalDateTime retentionCutoff = LocalDateTime.now().minusDays(cleanupDays);

        // Find completed or cancelled trips
        List<Trip> completedOrCancelled = tripRepository.findByStatusInAndIsDeletedFalse(
                List.of("COMPLETED", "CANCELLED")
        );

        for (Trip trip : completedOrCancelled) {
            LocalDateTime threshold = trip.getCompletedAt() != null ? trip.getCompletedAt() : trip.getUpdatedAt();
            if (threshold != null && threshold.isBefore(retentionCutoff)) {
                try {
                    gpsLocationRepository.deleteByTripIdAndTimestampBefore(trip.getId(), threshold);
                    log.info("Cleaned up old GPS telemetry history logs for completed/cancelled Trip ID: {}", trip.getId());
                } catch (Exception e) {
                    log.error("Failed to clean telemetry logs for Trip ID: {}. Error: {}", trip.getId(), e.getMessage());
                }
            }
        }
    }

    private EtaCalculationStrategy getEtaStrategy(String name) {
        if ("DEFAULT_TRAFFIC".equalsIgnoreCase(name)) {
            return new DefaultTrafficEtaStrategy();
        }
        return new AverageSpeedEtaStrategy();
    }

    private void createTimelineEvent(UUID tripId, String eventType, String title, String description) {
        TripTimeline timeline = TripTimeline.builder()
                .tripId(tripId)
                .eventType(eventType)
                .timestamp(LocalDateTime.now())
                .title(title)
                .description(description)
                .build();
        timelineRepository.save(timeline);
    }
}
