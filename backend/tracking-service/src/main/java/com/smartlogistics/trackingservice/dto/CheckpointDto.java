package com.smartlogistics.trackingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class CheckpointDto {

    public record CreateRequest(
            @NotBlank(message = "Checkpoint name is required")
            String name,
            @NotNull(message = "Latitude is required")
            Double latitude,
            @NotNull(message = "Longitude is required")
            Double longitude,
            @NotNull(message = "Sequence index is required")
            Integer sequenceIndex,
            @NotBlank(message = "Checkpoint type is required")
            String type, // PICKUP, LOADING_STARTED, LOADING_COMPLETED, EN_ROUTE, REST_STOP, DESTINATION, UNLOADING_STARTED, UNLOADING_COMPLETED
            @NotBlank(message = "Checkpoint status is required")
            String status, // PENDING, REACHED, DEPARTED
            LocalDateTime plannedArrivalTime
    ) {}

    public record Response(
            UUID id,
            String name,
            Double latitude,
            Double longitude,
            Integer sequenceIndex,
            String type,
            String status,
            LocalDateTime plannedArrivalTime,
            LocalDateTime actualArrivalTime,
            LocalDateTime departureTime
    ) {}
}
