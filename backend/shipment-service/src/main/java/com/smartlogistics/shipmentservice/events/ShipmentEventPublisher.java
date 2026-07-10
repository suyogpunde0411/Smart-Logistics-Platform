package com.smartlogistics.shipmentservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventPublisher {

    private static final String TOPIC_CREATED = "shipment.created";
    private static final String TOPIC_UPDATED = "shipment.updated";
    private static final String TOPIC_CANCELLED = "shipment.cancelled";
    private static final String TOPIC_READY_FOR_MATCHING = "shipment.ready-for-matching";
    private static final String TOPIC_DELIVERED = "shipment.delivered";
    private static final String TOPIC_COMPLETED = "shipment.completed";
    private static final String TOPIC_EXPIRED = "shipment.expired";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishShipmentCreated(UUID shipmentId, UUID businessOwnerId,
                                        String originAddress, String destinationAddress,
                                        Double totalWeight, String cargoType, Double budgetAmount) {
        String correlationId = UUID.randomUUID().toString();
        ShipmentCreatedKafkaEvent event = new ShipmentCreatedKafkaEvent(
                shipmentId, businessOwnerId, originAddress, destinationAddress,
                totalWeight, cargoType, budgetAmount, correlationId);
        publishSafely(TOPIC_CREATED, shipmentId.toString(), event, "ShipmentCreated");
    }

    public void publishShipmentUpdated(UUID shipmentId, UUID businessOwnerId, String oldStatus, String newStatus) {
        ShipmentStatusChangedEvent event = new ShipmentStatusChangedEvent(
                shipmentId, businessOwnerId, oldStatus, newStatus, "Shipment updated", UUID.randomUUID().toString());
        publishSafely(TOPIC_UPDATED, shipmentId.toString(), event, "ShipmentUpdated");
    }

    public void publishShipmentCancelled(UUID shipmentId, UUID businessOwnerId, String remarks) {
        ShipmentStatusChangedEvent event = new ShipmentStatusChangedEvent(
                shipmentId, businessOwnerId, null, "CANCELLED", remarks, UUID.randomUUID().toString());
        publishSafely(TOPIC_CANCELLED, shipmentId.toString(), event, "ShipmentCancelled");
    }

    public void publishShipmentReadyForMatching(UUID shipmentId, UUID businessOwnerId,
                                                 String originAddress, Double originLat, Double originLng,
                                                 String destinationAddress, Double destLat, Double destLng,
                                                 Double totalWeight, Double totalVolume, String cargoType,
                                                 String requiredTruckType, Double budgetAmount) {
        ShipmentReadyForMatchingEvent event = new ShipmentReadyForMatchingEvent(
                shipmentId, businessOwnerId, originAddress, originLat, originLng,
                destinationAddress, destLat, destLng, totalWeight, totalVolume,
                cargoType, requiredTruckType, budgetAmount, UUID.randomUUID().toString());
        publishSafely(TOPIC_READY_FOR_MATCHING, shipmentId.toString(), event, "ShipmentReadyForMatching");
    }

    public void publishShipmentDelivered(UUID shipmentId, UUID businessOwnerId) {
        ShipmentStatusChangedEvent event = new ShipmentStatusChangedEvent(
                shipmentId, businessOwnerId, "IN_TRANSIT", "DELIVERED", null, UUID.randomUUID().toString());
        publishSafely(TOPIC_DELIVERED, shipmentId.toString(), event, "ShipmentDelivered");
    }

    public void publishShipmentCompleted(UUID shipmentId, UUID businessOwnerId) {
        ShipmentStatusChangedEvent event = new ShipmentStatusChangedEvent(
                shipmentId, businessOwnerId, "DELIVERED", "COMPLETED", null, UUID.randomUUID().toString());
        publishSafely(TOPIC_COMPLETED, shipmentId.toString(), event, "ShipmentCompleted");
    }

    public void publishShipmentExpired(UUID shipmentId, UUID businessOwnerId) {
        ShipmentStatusChangedEvent event = new ShipmentStatusChangedEvent(
                shipmentId, businessOwnerId, "CREATED", "EXPIRED", "Auto-expired by system", UUID.randomUUID().toString());
        publishSafely(TOPIC_EXPIRED, shipmentId.toString(), event, "ShipmentExpired");
    }

    private void publishSafely(String topic, String key, Object event, String eventName) {
        try {
            kafkaTemplate.send(topic, key, event);
            log.info("Published {} to topic '{}' for key: {}", eventName, topic, key);
        } catch (Exception e) {
            log.error("Failed to publish {} to topic '{}': {}", eventName, topic, e.getMessage());
        }
    }
}
