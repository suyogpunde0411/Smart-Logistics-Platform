package com.smartlogistics.reviewservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.UUID;

@FeignClient(name = "tracking-service", path = "/api/v1/trips")
public interface TrackingFeignClient {

    @GetMapping("/{id}")
    TripResponse getTripById(@PathVariable("id") UUID id);

    @GetMapping("/{id}")
    InternalTripResponse getTrip(@PathVariable("id") UUID id);

    record TripResponse(
            UUID id,
            UUID shipmentId,
            UUID truckId,
            UUID driverId,
            UUID businessId,
            String status,
            LocalDateTime startedAt,
            LocalDateTime completedAt,
            Double totalDistanceTravelledKm
    ) {}

    record InternalTripResponse(
            UUID id,
            UUID shipmentId,
            UUID truckId,
            UUID driverId,
            UUID businessId,
            String status
    ) {}
}
