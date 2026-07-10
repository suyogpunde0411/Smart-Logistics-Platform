package com.smartlogistics.trackingservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class RestStopDto {

    public record CreateRequest(
            @NotNull(message = "Rest start time is required")
            LocalDateTime startTime,
            LocalDateTime endTime,
            @NotNull(message = "Latitude is required")
            Double latitude,
            @NotNull(message = "Longitude is required")
            Double longitude,
            String stopLocationName,
            String notes
    ) {}

    public record Response(
            UUID id,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Double latitude,
            Double longitude,
            String stopLocationName,
            String notes
    ) {}
}
