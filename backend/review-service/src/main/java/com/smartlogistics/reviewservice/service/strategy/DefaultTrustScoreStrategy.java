package com.smartlogistics.reviewservice.service.strategy;

import com.smartlogistics.reviewservice.entity.TrustScore;
import org.springframework.stereotype.Component;

@Component("defaultTrustScoreStrategy")
public class DefaultTrustScoreStrategy implements TrustScoreCalculationStrategy {

    @Override
    public int calculateScore(TrustScore entity) {
        if (entity == null) {
            return 100;
        }

        double score = 100.0;

        // Simple check: deduct (5.0 - rating) * 5
        if (entity.getAverageRating() != null) {
            score -= (5.0 - entity.getAverageRating()) * 5.0;
        }

        int finalScore = (int) Math.round(score);
        return Math.max(0, Math.min(finalScore, 100));
    }
}
