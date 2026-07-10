package com.smartlogistics.trackingservice.client;

import com.smartlogistics.trackingservice.config.TrackingFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "shipment-service", contextId = "trackingShipmentClient", configuration = TrackingFeignConfig.class)
public interface ShipmentClient {

    @GetMapping("/internal/shipments/{id}")
    InternalShipmentResponse getShipment(@PathVariable("id") UUID id);

    @GetMapping("/api/v1/shipments/{id}")
    DetailedShipmentResponse getShipmentDetailed(@PathVariable("id") UUID id);

    record InternalShipmentResponse(
            UUID id,
            UUID businessId,
            String origin,
            String destination,
            Double weight,
            Double budget,
            String status
    ) {}

    record DetailedShipmentResponse(
            UUID id,
            String trackingNumber,
            UUID businessOwnerId,
            String originAddress,
            Double originLatitude,
            Double originLongitude,
            String destinationAddress,
            Double destinationLatitude,
            Double destinationLongitude,
            String status
    ) {}
}
