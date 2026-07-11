package com.smartlogistics.reviewservice;

import com.smartlogistics.reviewservice.entity.TrustScore;
import com.smartlogistics.reviewservice.service.strategy.WeightedTrustScoreStrategy;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrustScoreStrategyTest {

    private final WeightedTrustScoreStrategy strategy = new WeightedTrustScoreStrategy();

    @Test
    public void testCalculateScore_PerfectConditions_Returns100() {
        TrustScore score = TrustScore.builder()
                .userId(UUID.randomUUID())
                .score(100)
                .averageRating(5.0)
                .completedTrips(10)
                .cancelledTrips(0)
                .reportedReviewsCount(0)
                .disputeCount(0)
                .build();

        int result = strategy.calculateScore(score);
        assertEquals(100, result);
    }

    @Test
    public void testCalculateScore_NegativeDeductions_ClampsToMinMax() {
        // High cancellations, reports, disputes, and poor rating
        TrustScore score = TrustScore.builder()
                .userId(UUID.randomUUID())
                .score(100)
                .averageRating(2.0) // deduction: (5.0-2.0)*10 = -30
                .completedTrips(0)
                .cancelledTrips(20) // deduction: -40
                .reportedReviewsCount(10) // deduction: -50
                .disputeCount(5) // deduction: -50
                .build();

        int result = strategy.calculateScore(score);
        // Base 100 - 30 - 40 - 50 - 50 = -70 -> Clamped to 0
        assertEquals(0, result);
    }

    @Test
    public void testCalculateScore_TripsCompletedBonus_CappedAt15() {
        TrustScore score = TrustScore.builder()
                .userId(UUID.randomUUID())
                .score(100)
                .averageRating(4.5) // deduction: -5
                .completedTrips(50) // bonus: 50 * 0.5 = 25 -> Capped at 15
                .cancelledTrips(0)
                .reportedReviewsCount(0)
                .disputeCount(0)
                .build();

        int result = strategy.calculateScore(score);
        // 100 - 5 + 15 = 110 -> Clamped to 100
        assertEquals(100, result);
    }
}
