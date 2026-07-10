package com.smartlogistics.trackingservice.events;

import com.smartlogistics.common.event.MatchAcceptedEvent;
import com.smartlogistics.common.event.TruckAvailabilityChangedEvent;
import com.smartlogistics.trackingservice.constants.TrackingConstants;
import com.smartlogistics.trackingservice.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingKafkaConsumer {

    private final TripService tripService;

    @KafkaListener(topics = TrackingConstants.TOPIC_BID_ACCEPTED, groupId = "tracking-service-group")
    public void consumeMatchAccepted(MatchAcceptedEvent event) {
        String cid = event.correlationId() != null ? event.correlationId() : event.eventId();
        MDC.put("correlationId", cid);
        try {
            log.info("Received MatchAcceptedEvent for match: {}, shipment: {}", event.matchId(), event.shipmentId());
            tripService.createTripFromMatch(event);
        } catch (Exception e) {
            log.error("Error processing MatchAcceptedEvent: {}", e.getMessage(), e);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = TrackingConstants.TOPIC_SHIPMENT_CANCELLED, groupId = "tracking-service-group")
    public void consumeShipmentCancelled(ShipmentStatusChangedEvent event) {
        String cid = event.correlationId() != null ? event.correlationId() : event.eventId().toString();
        MDC.put("correlationId", cid);
        try {
            log.info("Received ShipmentCancelledEvent for shipment: {}", event.shipmentId());
            tripService.cancelTripForShipment(event.shipmentId(), event.remarks() != null ? event.remarks() : "Shipment cancelled");
        } catch (Exception e) {
            log.error("Error processing ShipmentCancelledEvent: {}", e.getMessage(), e);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = TrackingConstants.TOPIC_TRUCK_AVAILABILITY_CHANGED, groupId = "tracking-service-group")
    public void consumeTruckAvailabilityChanged(TruckAvailabilityChangedEvent event) {
        String cid = event.correlationId() != null ? event.correlationId() : event.eventId();
        MDC.put("correlationId", cid);
        try {
            log.info("Received TruckAvailabilityChangedEvent for truck: {}, status: {}", event.truckId(), event.status());
            // No direct business logic mutation required for tracking-service here
            // Logs state check or triggers status synchronization checks
        } catch (Exception e) {
            log.error("Error processing TruckAvailabilityChangedEvent: {}", e.getMessage(), e);
        } finally {
            MDC.clear();
        }
    }
}
