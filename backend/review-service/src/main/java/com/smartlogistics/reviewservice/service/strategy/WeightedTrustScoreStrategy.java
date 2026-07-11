package com.smartlogistics.reviewservice.service.strategy;

import com.smartlogistics.reviewservice.entity.TrustScore;
import org.springframework.stereotype.Component;

@Component("weightedTrustScoreStrategy")
public class WeightedTrustScoreStrategy implements TrustScoreCalculationStrategy {

    @Override
    public int calculateScore(TrustScore entity) {
        if (entity == null) {
            return 100;
        }

        double score = 100.0;

        // 1. Average Rating: deduct (5.0 - rating) * 10 points
        if (entity.getAverageRating() != null) {
            score -= (5.0 - entity.getAverageRating()) * 10.0;
        }

        // 2. Completed Trips: +0.5 points per trip (capped at +15 points)
        if (entity.getCompletedTrips() != null) {
            double bonus = entity.getCompletedTrips() * 0.5;
            score += Math.min(bonus, 15.0);
        }

        // 3. Cancelled Trips: -2.0 points per trip
        if (entity.getCancelledTrips() != null) {
            score -= entity.getCancelledTrips() * 2.0;
        }

        // 4. Reported Reviews: -5.0 points per reported review
        if (entity.getReportedReviewsCount() != null) {
            score -= entity.getReportedReviewsCount() * 5.0;
        }

        // 5. Disputes: -10.0 points per dispute
        if (entity.getDisputeCount() != null) {
            score -= entity.getDisputeCount() * 10.0;
        }

        // Clamp score between 0 and 100
        int finalScore = (int) Math.round(score);
        return Math.max(0, Math.min(finalScore, 100));
    }
}
