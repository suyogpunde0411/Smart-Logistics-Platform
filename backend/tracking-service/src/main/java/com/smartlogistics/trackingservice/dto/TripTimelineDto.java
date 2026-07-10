package com.smartlogistics.trackingservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TripTimelineDto {

    public record Response(
            UUID id,
            UUID tripId,
            LocalDateTime timestamp,
            String title,
            String description,
            Double latitude,
            Double longitude,
            String eventType
    ) {}
}
