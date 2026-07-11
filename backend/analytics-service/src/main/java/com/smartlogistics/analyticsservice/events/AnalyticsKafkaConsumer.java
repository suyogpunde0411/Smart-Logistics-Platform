package com.smartlogistics.analyticsservice.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.common.event.*;
import com.smartlogistics.shared.event.MatchCreatedEvent;
import com.smartlogistics.analyticsservice.entity.*;
import com.smartlogistics.analyticsservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsKafkaConsumer {

    private final DashboardMetricRepository dashboardMetricRepository;
    private final DriverAnalyticsRepository driverAnalyticsRepository;
    private final BusinessAnalyticsRepository businessAnalyticsRepository;
    private final FleetAnalyticsRepository fleetAnalyticsRepository;
    private final TruckAnalyticsRepository truckAnalyticsRepository;
    private final ShipmentAnalyticsRepository shipmentAnalyticsRepository;
    private final TripAnalyticsRepository tripRepository;
    private final RevenueAnalyticsRepository revenueRepository;
    private final FuelAnalyticsRepository fuelRepository;
    private final CarbonSavingsRepository carbonRepository;
    private final MatchingAnalyticsRepository matchingRepository;
    private final ReviewAnalyticsRepository reviewRepository;
    private final DailyMetricsRepository dailyRepository;
    private final WeeklyMetricsRepository weeklyRepository;
    private final MonthlyMetricsRepository monthlyRepository;
    private final YearlyMetricsRepository yearlyRepository;
    private final KpiSnapshotRepository kpiRepository;

    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;

    @KafkaListener(topics = "user-registration", groupId = "analytics-service-group")
    @Transactional
    public void consumeUserRegistered(UserRegisteredEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming UserRegisteredEvent for user: {}", event.userId());
        try {
            String role = event.role();
            if ("DRIVER".equalsIgnoreCase(role)) {
                driverAnalyticsRepository.findByDriverIdAndIsDeletedFalse(event.userId())
                        .orElseGet(() -> driverAnalyticsRepository.save(DriverAnalytics.builder()
                                .driverId(event.userId())
                                .trustScore(100)
                                .averageRating(5.0)
                                .build()));
                incrementMetric("TOTAL_DRIVERS", 1.0);
            } else if ("BUSINESS_OWNER".equalsIgnoreCase(role)) {
                businessAnalyticsRepository.findByBusinessIdAndIsDeletedFalse(event.userId())
                        .orElseGet(() -> businessAnalyticsRepository.save(BusinessAnalytics.builder()
                                .businessId(event.userId())
                                .averageRating(5.0)
                                .build()));
                incrementMetric("TOTAL_BUSINESSES", 1.0);
            } else if ("FLEET_OWNER".equalsIgnoreCase(role)) {
                fleetAnalyticsRepository.findByFleetOwnerIdAndIsDeletedFalse(event.userId())
                        .orElseGet(() -> fleetAnalyticsRepository.save(FleetAnalytics.builder()
                                .fleetOwnerId(event.userId())
                                .build()));
                incrementMetric("TOTAL_FLEET_OWNERS", 1.0);
            }
            clearCache();
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "truck.registered", groupId = "analytics-service-group")
    @Transactional
    public void consumeTruckRegistered(TruckRegisteredEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming TruckRegisteredEvent for truck: {}", event.truckId());
        try {
            truckAnalyticsRepository.findByTruckIdAndIsDeletedFalse(event.truckId())
                    .orElseGet(() -> truckAnalyticsRepository.save(TruckAnalytics.builder()
                            .truckId(event.truckId())
                            .licensePlate(event.licensePlate())
                            .build()));

            if (event.ownerId() != null) {
                FleetAnalytics fleet = fleetAnalyticsRepository.findByFleetOwnerIdAndIsDeletedFalse(event.ownerId())
                        .orElseGet(() -> fleetAnalyticsRepository.save(FleetAnalytics.builder()
                                .fleetOwnerId(event.ownerId())
                                .build()));
                fleet.setTotalTrucks(fleet.getTotalTrucks() + 1);
                fleetAnalyticsRepository.save(fleet);
            }
            incrementMetric("TOTAL_TRUCKS", 1.0);
            clearCache();
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "shipment.created", groupId = "analytics-service-group")
    @Transactional
    public void consumeShipmentCreated(ShipmentCreatedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming ShipmentCreatedEvent for shipment: {}", event.shipmentId());
        try {
            shipmentAnalyticsRepository.findByShipmentIdAndIsDeletedFalse(event.shipmentId())
                    .orElseGet(() -> shipmentAnalyticsRepository.save(ShipmentAnalytics.builder()
                            .shipmentId(event.shipmentId())
                            .businessId(event.businessId())
                            .originAddress(event.origin())
                            .destinationAddress(event.destination())
                            .totalWeight(event.weight())
                            .budgetAmount(event.budget())
                            .status("CREATED")
                            .build()));

            if (event.businessId() != null) {
                businessAnalyticsRepository.findByBusinessIdAndIsDeletedFalse(event.businessId()).ifPresent(b -> {
                    b.setTotalShipments(b.getTotalShipments() + 1);
                    businessAnalyticsRepository.save(b);
                });
            }
            incrementMetric("TOTAL_SHIPMENTS", 1.0);
            clearCache();
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "shipment.completed", groupId = "analytics-service-group")
    @Transactional
    public void consumeShipmentCompleted(String message) {
        log.info("Consuming raw shipment.completed message");
        try {
            ShipmentStatusChangedEvent event = objectMapper.readValue(message, ShipmentStatusChangedEvent.class);
            setupMdc(event.correlationId(), event.eventId() != null ? event.eventId().toString() : null);

            if ("COMPLETED".equalsIgnoreCase(event.newStatus())) {
                shipmentAnalyticsRepository.findByShipmentIdAndIsDeletedFalse(event.shipmentId()).ifPresent(s -> {
                    s.setStatus("COMPLETED");
                    shipmentAnalyticsRepository.save(s);

                    if (s.getBusinessId() != null) {
                        businessAnalyticsRepository.findByBusinessIdAndIsDeletedFalse(s.getBusinessId()).ifPresent(b -> {
                            b.setCompletedShipments(b.getCompletedShipments() + 1);
                            if (s.getBudgetAmount() != null) {
                                b.setTotalBudgetSpent(b.getTotalBudgetSpent() + s.getBudgetAmount());
                            }
                            businessAnalyticsRepository.save(b);
                        });
                    }
                });
                incrementMetric("COMPLETED_SHIPMENTS", 1.0);
                clearCache();
            }
        } catch (Exception e) {
            log.error("Failed to parse shipment.completed event: {}", e.getMessage());
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "match.created", groupId = "analytics-service-group")
    @Transactional
    public void consumeMatchCreated(MatchCreatedEvent event) {
        setupMdc(event.getCorrelationId(), event.getEventId() != null ? event.getEventId().toString() : null);
        log.info("Consuming MatchCreatedEvent for match: {}", event.getMatchId());
        try {
            MatchingAnalytics ma = matchingRepository.findLatestAndIsDeletedFalse()
                    .orElseGet(() -> matchingRepository.save(MatchingAnalytics.builder().build()));

            ma.setTotalMatchesCreated(ma.getTotalMatchesCreated() + 1);
            recalculateMatchingSuccess(ma);
            matchingRepository.save(ma);
            clearCache();
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "bid.accepted", groupId = "analytics-service-group")
    @Transactional
    public void consumeMatchAccepted(MatchAcceptedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming MatchAcceptedEvent for match: {}", event.matchId());
        try {
            MatchingAnalytics ma = matchingRepository.findLatestAndIsDeletedFalse()
                    .orElseGet(() -> matchingRepository.save(MatchingAnalytics.builder().build()));

            ma.setTotalMatchesAccepted(ma.getTotalMatchesAccepted() + 1);
            recalculateMatchingSuccess(ma);
            matchingRepository.save(ma);
            clearCache();
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "trip.started", groupId = "analytics-service-group")
    @Transactional
    public void consumeTripStarted(TripStartedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming TripStartedEvent for trip: {}", event.tripId());
        try {
            UUID businessId = shipmentAnalyticsRepository.findByShipmentIdAndIsDeletedFalse(event.shipmentId())
                    .map(ShipmentAnalytics::getBusinessId)
                    .orElse(null);

            tripRepository.findByTripIdAndIsDeletedFalse(event.tripId())
                    .orElseGet(() -> tripRepository.save(TripAnalytics.builder()
                            .tripId(event.tripId())
                            .shipmentId(event.shipmentId())
                            .driverId(event.driverId())
                            .truckId(event.truckId())
                            .businessId(businessId)
                            .status("IN_TRANSIT")
                            .build()));

            if (event.driverId() != null) {
                driverAnalyticsRepository.findByDriverIdAndIsDeletedFalse(event.driverId()).ifPresent(da -> {
                    da.setTotalTrips(da.getTotalTrips() + 1);
                    driverAnalyticsRepository.save(da);
                });
            }

            if (event.truckId() != null) {
                truckAnalyticsRepository.findByTruckIdAndIsDeletedFalse(event.truckId()).ifPresent(ta -> {
                    ta.setTotalTrips(ta.getTotalTrips() + 1);
                    truckAnalyticsRepository.save(ta);
                });
            }
            incrementMetric("ACTIVE_TRIPS", 1.0);
            clearCache();
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "trip.completed", groupId = "analytics-service-group")
    @Transactional
    public void consumeTripCompleted(TripCompletedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming TripCompletedEvent for trip: {}", event.tripId());
        try {
            double distance = event.finalDistance() != null ? event.finalDistance() : 0.0;
            double fuel = distance * 0.15; // Simulated: 15L per 100km
            double carbon = distance * 0.22; // Simulated: 220g CO2 per km saved

            TripAnalytics trip = tripRepository.findByTripIdAndIsDeletedFalse(event.tripId())
                    .orElseGet(() -> TripAnalytics.builder().tripId(event.tripId()).build());

            trip.setStatus("COMPLETED");
            trip.setDistanceKm(distance);
            trip.setFuelConsumedLiters(fuel);
            trip.setCarbonSavingsKg(carbon);

            LocalDateTime end = event.endTimestamp() != null 
                    ? LocalDateTime.ofInstant(event.endTimestamp(), ZoneId.systemDefault()) 
                    : LocalDateTime.now();
            LocalDateTime start = trip.getCreatedAt() != null ? trip.getCreatedAt() : end.minusHours(2);
            double durationMinutes = java.time.Duration.between(start, end).toMinutes();
            trip.setDurationMinutes(durationMinutes);
            tripRepository.save(trip);

            // Fetch dynamic revenue from shipment budget
            double revenueAmount = shipmentAnalyticsRepository.findByShipmentIdAndIsDeletedFalse(trip.getShipmentId())
                    .map(ShipmentAnalytics::getBudgetAmount)
                    .orElse(150.0); // fallback

            // Update driver analytics
            if (trip.getDriverId() != null) {
                driverAnalyticsRepository.findByDriverIdAndIsDeletedFalse(trip.getDriverId()).ifPresent(da -> {
                    da.setCompletedTrips(da.getCompletedTrips() + 1);
                    da.setTotalDistanceTravelled(da.getTotalDistanceTravelled() + distance);
                    da.setTotalDurationMinutes(da.getTotalDurationMinutes() + durationMinutes);
                    da.setTotalRevenue(da.getTotalRevenue() + revenueAmount);
                    da.setAverageDeliveryTimeMinutes(da.getTotalDurationMinutes() / da.getCompletedTrips());
                    driverAnalyticsRepository.save(da);
                });
            }

            // Update truck analytics
            if (trip.getTruckId() != null) {
                truckAnalyticsRepository.findByTruckIdAndIsDeletedFalse(trip.getTruckId()).ifPresent(ta -> {
                    ta.setTotalDistanceKm(ta.getTotalDistanceKm() + distance);
                    ta.setFuelConsumedLiters(ta.getFuelConsumedLiters() + fuel);
                    ta.setCarbonSavingsKg(ta.getCarbonSavingsKg() + carbon);
                    ta.setActiveHours(ta.getActiveHours() + (durationMinutes / 60.0));
                    truckAnalyticsRepository.save(ta);
                });
            }

            // Update daily revenue stats
            LocalDate today = LocalDate.now();
            revenueRepository.findByDateAndIsDeletedFalse(today)
                    .or(() -> Optional.of(RevenueAnalytics.builder().date(today).build()))
                    .ifPresent(rev -> {
                        rev.setRevenue(rev.getRevenue() + revenueAmount);
                        rev.setShipmentCount(rev.getShipmentCount() + 1);
                        revenueRepository.save(rev);
                    });

            // Update daily performance snapshot metrics
            dailyRepository.findByDateAndIsDeletedFalse(today)
                    .or(() -> Optional.of(DailyMetrics.builder().date(today).build()))
                    .ifPresent(dm -> {
                        dm.setCompletedTrips(dm.getCompletedTrips() + 1);
                        dm.setTotalTrips(dm.getTotalTrips() + 1);
                        dm.setRevenue(dm.getRevenue() + revenueAmount);
                        dm.setCarbonSavedKg(dm.getCarbonSavedKg() + carbon);
                        dailyRepository.save(dm);
                    });

            incrementMetric("COMPLETED_TRIPS", 1.0);
            incrementMetric("TOTAL_REVENUE", revenueAmount);
            decrementMetric("ACTIVE_TRIPS", 1.0);
            clearCache();
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "trip.cancelled", groupId = "analytics-service-group")
    @Transactional
    public void consumeTripCancelled(String message) {
        log.info("Consuming raw trip.cancelled message");
        try {
            TripCancelledEvent event = objectMapper.readValue(message, TripCancelledEvent.class);
            setupMdc(event.correlationId(), event.eventId());

            tripRepository.findByTripIdAndIsDeletedFalse(event.tripId()).ifPresent(t -> {
                t.setStatus("CANCELLED");
                tripRepository.save(t);

                if (t.getDriverId() != null) {
                    driverAnalyticsRepository.findByDriverIdAndIsDeletedFalse(t.getDriverId()).ifPresent(da -> {
                        da.setCancelledTrips(da.getCancelledTrips() + 1);
                        driverAnalyticsRepository.save(da);
                    });
                }
                if (t.getBusinessId() != null) {
                    businessAnalyticsRepository.findByBusinessIdAndIsDeletedFalse(t.getBusinessId()).ifPresent(ba -> {
                        ba.setCancelledShipments(ba.getCancelledShipments() + 1);
                        businessAnalyticsRepository.save(ba);
                    });
                }
            });
            incrementMetric("CANCELLED_TRIPS", 1.0);
            decrementMetric("ACTIVE_TRIPS", 1.0);
            clearCache();
        } catch (Exception e) {
            log.error("Failed to parse trip.cancelled event: {}", e.getMessage());
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "review.created", groupId = "analytics-service-group")
    @Transactional
    public void consumeReviewCreated(ReviewCreatedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming ReviewCreatedEvent for review: {}", event.reviewId());
        try {
            reviewRepository.findByRevieweeIdAndIsDeletedFalse(event.revieweeId())
                    .or(() -> Optional.of(ReviewAnalytics.builder().revieweeId(event.revieweeId()).build()))
                    .ifPresent(ra -> {
                        double ratingVal = event.rating() != null ? event.rating().doubleValue() : 5.0;
                        double totalRatingSum = (ra.getAverageRating() * ra.getTotalReviews()) + ratingVal;
                        ra.setTotalReviews(ra.getTotalReviews() + 1);
                        ra.setAverageRating(totalRatingSum / ra.getTotalReviews());
                        reviewRepository.save(ra);

                        // Sync with specific profile caches
                        driverAnalyticsRepository.findByDriverIdAndIsDeletedFalse(event.revieweeId()).ifPresent(da -> {
                            da.setAverageRating(ra.getAverageRating());
                            driverAnalyticsRepository.save(da);
                        });
                        businessAnalyticsRepository.findByBusinessIdAndIsDeletedFalse(event.revieweeId()).ifPresent(ba -> {
                            ba.setAverageRating(ra.getAverageRating());
                            businessAnalyticsRepository.save(ba);
                        });
                    });
            clearCache();
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "trust-score.updated", groupId = "analytics-service-group")
    @Transactional
    public void consumeTrustScoreUpdated(String message) {
        log.info("Consuming raw trust-score.updated message");
        try {
            TrustScoreUpdatedEvent event = objectMapper.readValue(message, TrustScoreUpdatedEvent.class);
            setupMdc(event.correlationId(), event.eventId());

            driverAnalyticsRepository.findByDriverIdAndIsDeletedFalse(event.userId()).ifPresent(da -> {
                da.setTrustScore(event.newScore());
                driverAnalyticsRepository.save(da);
            });
            clearCache();
        } catch (Exception e) {
            log.error("Failed to parse trust-score.updated event: {}", e.getMessage());
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "notification.sent", groupId = "analytics-service-group")
    @Transactional
    public void consumeNotificationSent(String message) {
        log.info("Consuming raw notification.sent message");
        try {
            NotificationSentEvent event = objectMapper.readValue(message, NotificationSentEvent.class);
            setupMdc(event.correlationId(), event.eventId());

            incrementMetric("TOTAL_NOTIFICATIONS_SENT", 1.0);
            clearCache();
        } catch (Exception e) {
            log.error("Failed to parse notification.sent event: {}", e.getMessage());
        } finally {
            MDC.clear();
        }
    }

    private void incrementMetric(String metricKey, double value) {
        DashboardMetric dm = dashboardMetricRepository.findByMetricKeyAndIsDeletedFalse(metricKey)
                .orElseGet(() -> DashboardMetric.builder().metricKey(metricKey).metricValue(0.0).build());
        dm.setMetricValue(dm.getMetricValue() + value);
        dm.setLastUpdated(LocalDateTime.now());
        dashboardMetricRepository.save(dm);
    }

    private void decrementMetric(String metricKey, double value) {
        DashboardMetric dm = dashboardMetricRepository.findByMetricKeyAndIsDeletedFalse(metricKey)
                .orElseGet(() -> DashboardMetric.builder().metricKey(metricKey).metricValue(0.0).build());
        dm.setMetricValue(Math.max(0.0, dm.getMetricValue() - value));
        dm.setLastUpdated(LocalDateTime.now());
        dashboardMetricRepository.save(dm);
    }

    private void recalculateMatchingSuccess(MatchingAnalytics ma) {
        if (ma.getTotalMatchesCreated() > 0) {
            double rate = ((double) ma.getTotalMatchesAccepted() / ma.getTotalMatchesCreated()) * 100.0;
            ma.setSuccessRate(Math.round(rate * 100.0) / 100.0);
        } else {
            ma.setSuccessRate(0.0);
        }
    }

    private void setupMdc(String correlationId, String eventId) {
        MDC.clear();
        if (correlationId != null) MDC.put("correlationId", correlationId);
        if (eventId != null) MDC.put("traceId", eventId);
    }

    private void clearCache() {
        Optional.ofNullable(cacheManager.getCache("dashboards")).ifPresent(org.springframework.cache.Cache::clear);
    }
}
