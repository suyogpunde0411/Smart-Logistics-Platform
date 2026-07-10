package com.smartlogistics.trackingservice.events;

import com.smartlogistics.common.event.TripCompletedEvent;
import com.smartlogistics.common.event.TripStartedEvent;
import com.smartlogistics.trackingservice.constants.TrackingConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishTripCreated(UUID tripId, UUID shipmentId, UUID truckId, UUID driverId, UUID businessId) {
        String correlationId = getOrGenerateCorrelationId();
        TripCreatedEvent event = new TripCreatedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                tripId,
                shipmentId,
                truckId,
                driverId,
                businessId
        );
        send(TrackingConstants.TOPIC_TRIP_CREATED, tripId.toString(), event);
    }

    public void publishTripStarted(UUID tripId, UUID shipmentId, UUID driverId, UUID truckId) {
        String correlationId = getOrGenerateCorrelationId();
        TripStartedEvent event = new TripStartedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                tripId,
                shipmentId,
                driverId,
                truckId
        );
        send(TrackingConstants.TOPIC_TRIP_STARTED, tripId.toString(), event);
    }

    public void publishTripPaused(UUID tripId) {
        String correlationId = getOrGenerateCorrelationId();
        TripPausedEvent event = new TripPausedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                tripId
        );
        send(TrackingConstants.TOPIC_TRIP_PAUSED, tripId.toString(), event);
    }

    public void publishTripResumed(UUID tripId) {
        String correlationId = getOrGenerateCorrelationId();
        TripResumedEvent event = new TripResumedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                tripId
        );
        send(TrackingConstants.TOPIC_TRIP_RESUMED, tripId.toString(), event);
    }

    public void publishTripCompleted(UUID tripId, UUID shipmentId, UUID driverId, UUID truckId, Double finalDistance) {
        String correlationId = getOrGenerateCorrelationId();
        TripCompletedEvent event = new TripCompletedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                tripId,
                shipmentId,
                driverId,
                truckId,
                finalDistance,
                Instant.now()
        );
        send(TrackingConstants.TOPIC_TRIP_COMPLETED, tripId.toString(), event);
    }

    public void publishTripCancelled(UUID tripId, String reason) {
        String correlationId = getOrGenerateCorrelationId();
        TripCancelledEvent event = new TripCancelledEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                tripId,
                reason
        );
        send(TrackingConstants.TOPIC_TRIP_CANCELLED, tripId.toString(), event);
    }

    public void publishGpsUpdated(UUID tripId, Double latitude, Double longitude, Double speedKmh, Double heading, Double distanceTravelledKm) {
        String correlationId = getOrGenerateCorrelationId();
        GpsUpdatedEvent event = new GpsUpdatedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                tripId,
                latitude,
                longitude,
                speedKmh,
                heading,
                distanceTravelledKm
        );
        send(TrackingConstants.TOPIC_GPS_UPDATED, tripId.toString(), event);
    }

    public void publishEtaUpdated(UUID tripId, LocalDateTime expectedArrivalTime, Double remainingDistanceKm, Double remainingTimeHours) {
        String correlationId = getOrGenerateCorrelationId();
        EtaUpdatedEvent event = new EtaUpdatedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                tripId,
                expectedArrivalTime,
                remainingDistanceKm,
                remainingTimeHours
        );
        send(TrackingConstants.TOPIC_ETA_UPDATED, tripId.toString(), event);
    }

    private void send(String topic, String key, Object value) {
        log.debug("Sending message to topic: {}, key: {}, payload: {}", topic, key, value);
        kafkaTemplate.send(topic, key, value).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send message to topic: {} key: {} due to: {}", topic, key, ex.getMessage());
            } else {
                log.info("Successfully sent message to topic: {} key: {}", topic, key);
            }
        });
    }

    private String getOrGenerateCorrelationId() {
        String cid = MDC.get("correlationId");
        return cid != null ? cid : UUID.randomUUID().toString();
    }
}
