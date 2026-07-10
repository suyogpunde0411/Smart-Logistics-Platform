package com.smartlogistics.matchingservice.events;

import com.smartlogistics.common.event.TripCompletedEvent;
import com.smartlogistics.common.event.TruckAvailabilityChangedEvent;
import com.smartlogistics.matchingservice.service.BidService;
import com.smartlogistics.matchingservice.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingKafkaConsumer {

    private final MatchingService matchingService;
    private final BidService bidService;

    @KafkaListener(topics = "shipment.ready-for-matching", groupId = "matching-service-group")
    public void consumeShipmentReady(ShipmentReadyForMatchingEvent event) {
        MDC.put("correlationId", event.correlationId());
        try {
            log.info("Received ShipmentReadyForMatchingEvent for shipment: {}", event.shipmentId());
            matchingService.processAutomaticMatching(event);
        } catch (Exception e) {
            log.error("Error processing ShipmentReadyForMatchingEvent for shipment: {}", event.shipmentId(), e);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "shipment.cancelled", groupId = "matching-service-group")
    public void consumeShipmentCancelled(ShipmentStatusChangedEvent event) {
        MDC.put("correlationId", event.correlationId());
        try {
            log.info("Received ShipmentCancelledEvent for shipment: {}", event.shipmentId());
            matchingService.cancelMatchesForShipment(event.shipmentId(), event.remarks());
        } catch (Exception e) {
            log.error("Error processing ShipmentCancelledEvent for shipment: {}", event.shipmentId(), e);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "truck.availability.changed", groupId = "matching-service-group")
    public void consumeTruckAvailabilityChanged(TruckAvailabilityChangedEvent event) {
        MDC.put("correlationId", event.correlationId());
        try {
            log.info("Received TruckAvailabilityChangedEvent for truck: {}, status: {}", event.truckId(), event.status());
            matchingService.processTruckAvailabilityChange(event.truckId(), event.status());
        } catch (Exception e) {
            log.error("Error processing TruckAvailabilityChangedEvent for truck: {}", event.truckId(), e);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "truck.deleted", groupId = "matching-service-group")
    public void consumeTruckDeleted(TruckDeletedEvent event) {
        MDC.put("correlationId", event.correlationId());
        try {
            log.info("Received TruckDeletedEvent for truck: {}", event.truckId());
            matchingService.processTruckDeletion(event.truckId());
        } catch (Exception e) {
            log.error("Error processing TruckDeletedEvent for truck: {}", event.truckId(), e);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "trip.completed", groupId = "matching-service-group")
    public void consumeTripCompleted(TripCompletedEvent event) {
        MDC.put("correlationId", event.correlationId());
        try {
            log.info("Received TripCompletedEvent for trip: {}, shipment: {}", event.tripId(), event.shipmentId());
            bidService.completeMatchWorkflow(event.shipmentId(), event.truckId());
        } catch (Exception e) {
            log.error("Error processing TripCompletedEvent for shipment: {}", event.shipmentId(), e);
        } finally {
            MDC.clear();
        }
    }
}
