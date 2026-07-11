package com.smartlogistics.reviewservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TrustScoreDto {

    public record TrustScoreResponse(
            UUID id,
            UUID userId,
            Integer score,
            Double averageRating,
            Integer completedTrips,
            Integer cancelledTrips,
            Integer reportedReviewsCount,
            Integer disputeCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    public record TrustScoreHistoryResponse(
            UUID id,
            UUID userId,
            Integer oldScore,
            Integer newScore,
            String reason,
            LocalDateTime createdAt
    ) {}
}
