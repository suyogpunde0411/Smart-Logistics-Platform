package com.smartlogistics.trackingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class TripDelayDto {

    public record CreateRequest(
            @NotNull(message = "Delay start time is required")
            LocalDateTime startTime,
            LocalDateTime endTime,
            @NotBlank(message = "Delay category is required")
            String category, // TRAFFIC, WEATHER, CUSTOMS, MECHANICAL, ACCIDENT, OTHER
            @NotBlank(message = "Delay reason is required")
            String reason,
            Double latitude,
            Double longitude
    ) {}

    public record Response(
            UUID id,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Double durationMinutes,
            String category,
            String reason,
            Double latitude,
            Double longitude
    ) {}
}
