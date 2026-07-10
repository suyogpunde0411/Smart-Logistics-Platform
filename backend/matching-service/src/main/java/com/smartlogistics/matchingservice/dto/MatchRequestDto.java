package com.smartlogistics.matchingservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public final class MatchRequestDto {

    private MatchRequestDto() {}

    public record CreateRequest(
            @NotNull(message = "Shipment ID is required")
            UUID shipmentId,

            Double radiusKm,
            Double maxDistanceKm,
            Integer ttlMinutes
    ) {}

    public record Response(
            UUID id,
            UUID shipmentId,
            UUID businessId,
            String status,
            Double pickupLatitude,
            Double pickupLongitude,
            Double destinationLatitude,
            Double destinationLongitude,
            Double radiusKm,
            Double maxDistanceKm,
            LocalDateTime expiresAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
