package com.smartlogistics.trackingservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class FuelLogDto {

    public record CreateRequest(
            @NotNull(message = "Fuel volume in liters is required")
            Double fuelVolumeLiters,
            @NotNull(message = "Cost per liter is required")
            Double costPerLiter,
            @NotNull(message = "Current odometer reading is required")
            Double odometerKm,
            String stationName,
            String stationLocation,
            @NotNull(message = "Refueling timestamp is required")
            LocalDateTime refueledAt
    ) {}

    public record Response(
            UUID id,
            Double fuelVolumeLiters,
            Double costPerLiter,
            Double totalCost,
            Double odometerKm,
            String stationName,
            String stationLocation,
            LocalDateTime refueledAt
    ) {}
}
