package com.smartlogistics.reviewservice.service.strategy;

import com.smartlogistics.reviewservice.entity.TrustScore;

public interface TrustScoreCalculationStrategy {
    int calculateScore(TrustScore scoreEntity);
}
