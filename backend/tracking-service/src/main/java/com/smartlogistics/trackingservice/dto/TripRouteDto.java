package com.smartlogistics.trackingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class TripRouteDto {

    public record CreateRequest(
            @NotBlank(message = "Start address is required")
            String startAddress,
            @NotNull(message = "Start latitude is required")
            Double startLatitude,
            @NotNull(message = "Start longitude is required")
            Double startLongitude,
            @NotBlank(message = "End address is required")
            String endAddress,
            @NotNull(message = "End latitude is required")
            Double endLatitude,
            @NotNull(message = "End longitude is required")
            Double endLongitude,
            Double plannedDistanceKm,
            Double plannedDurationHours
    ) {}

    public record Response(
            UUID id,
            String startAddress,
            Double startLatitude,
            Double startLongitude,
            String endAddress,
            Double endLatitude,
            Double endLongitude,
            Double plannedDistanceKm,
            Double plannedDurationHours
    ) {}
}
