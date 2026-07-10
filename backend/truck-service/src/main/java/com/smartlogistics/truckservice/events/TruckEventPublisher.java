package com.smartlogistics.truckservice.events;

import com.smartlogistics.common.event.TruckAvailabilityChangedEvent;
import com.smartlogistics.common.event.TruckRegisteredEvent;
import com.smartlogistics.common.event.TruckUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TruckEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishTruckRegistered(UUID truckId, UUID ownerId, String licensePlate, String type, Double capacity) {
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        TruckRegisteredEvent event = new TruckRegisteredEvent(
                eventId,
                Instant.now(),
                correlationId,
                truckId,
                ownerId,
                licensePlate,
                type,
                capacity
        );
        try {
            kafkaTemplate.send("truck.registered", truckId.toString(), event);
            log.info("Published TruckRegisteredEvent to topic 'truck.registered' for truck: {}", truckId);
        } catch (Exception e) {
            log.error("Failed to publish TruckRegisteredEvent: {}", e.getMessage());
        }
    }

    public void publishTruckUpdated(UUID truckId, String licensePlate, String type, Double capacity) {
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        TruckUpdatedEvent event = new TruckUpdatedEvent(
                eventId,
                Instant.now(),
                correlationId,
                truckId,
                licensePlate,
                type,
                capacity
        );
        try {
            kafkaTemplate.send("truck.updated", truckId.toString(), event);
            log.info("Published TruckUpdatedEvent to topic 'truck.updated' for truck: {}", truckId);
        } catch (Exception e) {
            log.error("Failed to publish TruckUpdatedEvent: {}", e.getMessage());
        }
    }

    public void publishTruckDeleted(UUID truckId) {
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        TruckDeletedEvent event = new TruckDeletedEvent(
                eventId,
                Instant.now(),
                correlationId,
                truckId
        );
        try {
            kafkaTemplate.send("truck.deleted", truckId.toString(), event);
            log.info("Published TruckDeletedEvent to topic 'truck.deleted' for truck: {}", truckId);
        } catch (Exception e) {
            log.error("Failed to publish TruckDeletedEvent: {}", e.getMessage());
        }
    }

    public void publishTruckAvailabilityChanged(UUID truckId, String status, Double lat, Double lng) {
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        TruckAvailabilityChangedEvent event = new TruckAvailabilityChangedEvent(
                eventId,
                Instant.now(),
                correlationId,
                truckId,
                status,
                lat,
                lng
        );
        try {
            kafkaTemplate.send("truck.availability.changed", truckId.toString(), event);
            log.info("Published TruckAvailabilityChangedEvent to topic 'truck.availability.changed' for truck: {}", truckId);
        } catch (Exception e) {
            log.error("Failed to publish TruckAvailabilityChangedEvent: {}", e.getMessage());
        }
    }

    public void publishTruckLocationUpdated(UUID truckId, Double lat, Double lng, Double speed, Double heading) {
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        TruckLocationUpdatedEvent event = new TruckLocationUpdatedEvent(
                eventId,
                Instant.now(),
                correlationId,
                truckId,
                lat,
                lng,
                speed,
                heading
        );
        try {
            kafkaTemplate.send("truck.location.updated", truckId.toString(), event);
            log.info("Published TruckLocationUpdatedEvent to topic 'truck.location.updated' for truck: {}", truckId);
        } catch (Exception e) {
            log.error("Failed to publish TruckLocationUpdatedEvent: {}", e.getMessage());
        }
    }

    public void publishTruckMaintenanceScheduled(UUID truckId, UUID maintenanceId, String date, Double cost) {
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        TruckMaintenanceScheduledEvent event = new TruckMaintenanceScheduledEvent(
                eventId,
                Instant.now(),
                correlationId,
                truckId,
                maintenanceId,
                date,
                cost
        );
        try {
            kafkaTemplate.send("truck.maintenance.scheduled", truckId.toString(), event);
            log.info("Published TruckMaintenanceScheduledEvent to topic 'truck.maintenance.scheduled' for truck: {}", truckId);
        } catch (Exception e) {
            log.error("Failed to publish TruckMaintenanceScheduledEvent: {}", e.getMessage());
        }
    }

    public void publishTruckInsuranceExpired(UUID truckId, String policyNumber, String expiryDate) {
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        TruckInsuranceExpiredEvent event = new TruckInsuranceExpiredEvent(
                eventId,
                Instant.now(),
                correlationId,
                truckId,
                policyNumber,
                expiryDate
        );
        try {
            kafkaTemplate.send("truck.insurance.expired", truckId.toString(), event);
            log.info("Published TruckInsuranceExpiredEvent to topic 'truck.insurance.expired' for truck: {}", truckId);
        } catch (Exception e) {
            log.error("Failed to publish TruckInsuranceExpiredEvent: {}", e.getMessage());
        }
    }
}
