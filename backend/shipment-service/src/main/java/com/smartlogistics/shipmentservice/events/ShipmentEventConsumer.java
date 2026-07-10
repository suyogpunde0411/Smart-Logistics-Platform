package com.smartlogistics.shipmentservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventConsumer {

    /**
     * Consumes BusinessProfileUpdated events.
     * Could be used to update cached business info or trigger shipment visibility changes.
     */
    @KafkaListener(topics = "business.profile.updated", groupId = "shipment-service-group")
    public void onBusinessProfileUpdated(Map<String, Object> payload) {
        try {
            log.info("Received BusinessProfileUpdated event: {}", payload);
            // Future: invalidate business owner cache, update profile-linked shipments if needed
        } catch (Exception e) {
            log.error("Error processing BusinessProfileUpdated event: {}", e.getMessage());
        }
    }

    /**
     * Consumes TruckAvailabilityChanged events.
     * Could be used for matching-readiness checks or availability tracking.
     */
    @KafkaListener(topics = "truck.availability.changed", groupId = "shipment-service-group")
    public void onTruckAvailabilityChanged(Map<String, Object> payload) {
        try {
            log.info("Received TruckAvailabilityChanged event: {}", payload);
            // Future: use to update matching eligibility status for shipments
        } catch (Exception e) {
            log.error("Error processing TruckAvailabilityChanged event: {}", e.getMessage());
        }
    }
}
