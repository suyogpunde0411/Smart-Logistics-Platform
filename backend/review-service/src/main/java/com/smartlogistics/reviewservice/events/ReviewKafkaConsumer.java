package com.smartlogistics.reviewservice.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.common.event.TripCompletedEvent;
import com.smartlogistics.reviewservice.client.TrackingFeignClient;
import com.smartlogistics.reviewservice.service.ReviewService;
import com.smartlogistics.reviewservice.service.TrustScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewKafkaConsumer {

    private final TrustScoreService trustScoreService;
    private final ReviewService reviewService;
    private final TrackingFeignClient trackingClient;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "trip.completed", groupId = "review-service-group")
    public void consumeTripCompleted(TripCompletedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming TripCompletedEvent for trip: {}", event.tripId());
        try {
            // 1. Process Driver
            if (event.driverId() != null) {
                trustScoreService.handleTripCompleted(event.driverId());
            }

            // 2. Process Business Owner (query via tracking Feign)
            try {
                TrackingFeignClient.InternalTripResponse trip = trackingClient.getTrip(event.tripId());
                if (trip != null && trip.businessId() != null) {
                    trustScoreService.handleTripCompleted(trip.businessId());
                }
            } catch (Exception ex) {
                log.warn("Could not retrieve business owner for trust score completed trip update: {}", ex.getMessage());
            }
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "trip.cancelled", groupId = "review-service-group")
    public void consumeTripCancelled(String message) {
        log.info("Consuming raw trip.cancelled message: {}", message);
        try {
            TripCancelledEvent event = objectMapper.readValue(message, TripCancelledEvent.class);
            setupMdc(event.correlationId(), event.eventId());
            log.info("Consuming TripCancelledEvent for trip: {}", event.tripId());

            // Retrieve participants via Feign
            try {
                TrackingFeignClient.InternalTripResponse trip = trackingClient.getTrip(event.tripId());
                if (trip != null) {
                    if (trip.driverId() != null) {
                        trustScoreService.handleTripCancelled(trip.driverId());
                    }
                    if (trip.businessId() != null) {
                        trustScoreService.handleTripCancelled(trip.businessId());
                    }
                }
            } catch (Exception ex) {
                log.warn("Could not retrieve trip details for cancellation trust score deduct: {}", ex.getMessage());
            }
        } catch (Exception e) {
            log.error("Failed to parse trip.cancelled event: {}", e.getMessage());
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "user.deleted", groupId = "review-service-group")
    public void consumeUserDeleted(String message) {
        log.info("Consuming raw user.deleted message: {}", message);
        try {
            UserDeletedEvent event = objectMapper.readValue(message, UserDeletedEvent.class);
            setupMdc(event.correlationId(), event.eventId());
            log.info("Consuming UserDeletedEvent for user: {}", event.userId());

            reviewService.handleUserDeleted(event.userId());
        } catch (Exception e) {
            log.error("Failed to parse user.deleted event: {}", e.getMessage());
        } finally {
            MDC.clear();
        }
    }

    private void setupMdc(String correlationId, Object eventId) {
        MDC.clear();
        if (correlationId != null) MDC.put("correlationId", correlationId);
        if (eventId != null) MDC.put("traceId", eventId.toString());
    }
}
