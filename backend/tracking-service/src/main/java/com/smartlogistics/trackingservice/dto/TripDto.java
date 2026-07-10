package com.smartlogistics.trackingservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TripDto {

    public record CreateRequest(
            @NotNull(message = "Shipment ID is required")
            UUID shipmentId,
            @NotNull(message = "Truck ID is required")
            UUID truckId,
            @NotNull(message = "Driver ID is required")
            UUID driverId,
            @NotNull(message = "Business ID is required")
            UUID businessId,
            @NotNull(message = "Route is required")
            TripRouteDto.CreateRequest route,
            List<CheckpointDto.CreateRequest> checkpoints
    ) {}

    public record AssignRequest(
            @NotNull(message = "Driver ID is required")
            UUID driverId,
            @NotNull(message = "Truck ID is required")
            UUID truckId
    ) {}

    public record Response(
            UUID id,
            UUID shipmentId,
            UUID truckId,
            UUID driverId,
            UUID businessId,
            String status,
            LocalDateTime startedAt,
            LocalDateTime completedAt,
            Double totalDistanceTravelledKm,
            Double currentLatitude,
            Double currentLongitude,
            LocalDateTime lastGpsUpdatedAt,
            LocalDateTime expectedArrivalTime,
            Double remainingDistanceKm,
            Double averageSpeedKmh,
            TripRouteDto.Response route,
            List<CheckpointDto.Response> checkpoints,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
