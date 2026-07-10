package com.smartlogistics.trackingservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class GpsLocationDto {

    public record CreateRequest(
            @NotNull(message = "Latitude is required")
            Double latitude,
            @NotNull(message = "Longitude is required")
            Double longitude,
            Double speedKmh,
            Double heading,
            Double accuracy,
            Double altitude,
            @NotNull(message = "Timestamp is required")
            LocalDateTime timestamp
    ) {}

    public record Response(
            UUID id,
            Double latitude,
            Double longitude,
            Double speedKmh,
            Double heading,
            Double accuracy,
            Double altitude,
            Double distanceTravelledKm,
            LocalDateTime timestamp
    ) {}
}
