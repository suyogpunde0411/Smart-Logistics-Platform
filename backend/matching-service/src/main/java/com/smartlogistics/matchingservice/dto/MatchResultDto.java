package com.smartlogistics.matchingservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class MatchResultDto {

    private MatchResultDto() {}

    public record Response(
            UUID id,
            UUID matchRequestId,
            UUID shipmentId,
            UUID truckId,
            UUID driverId,
            Double overallScore,
            Double distanceScore,
            Double capacityScore,
            Double availabilityScore,
            Double compatibilityScore,
            Double estimatedEtaMinutes,
            Double estimatedCost,
            String reasoning,
            String status,
            LocalDateTime expiresAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    public record ScoreDetails(
            Double overallScore,
            Double distanceScore,
            Double capacityScore,
            Double availabilityScore,
            Double compatibilityScore,
            Double estimatedEtaMinutes,
            Double estimatedCost,
            String reasoning
    ) {}
}
