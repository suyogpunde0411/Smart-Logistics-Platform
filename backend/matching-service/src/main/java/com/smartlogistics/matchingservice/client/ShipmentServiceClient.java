package com.smartlogistics.matchingservice.client;

import com.smartlogistics.matchingservice.config.MatchingFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.UUID;

@FeignClient(name = "shipment-service", contextId = "matchingShipmentServiceClient", configuration = MatchingFeignConfig.class)
public interface ShipmentServiceClient {

    @GetMapping("/api/v1/shipments/{id}")
    DetailedShipmentResponse getShipmentById(@PathVariable("id") UUID id);

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
            String status,
            String cargoType,
            Double totalWeight,
            Double totalVolume,
            String requiredTruckType,
            Double budgetAmount,
            LocalDateTime expiresAt
    ) {}
}
