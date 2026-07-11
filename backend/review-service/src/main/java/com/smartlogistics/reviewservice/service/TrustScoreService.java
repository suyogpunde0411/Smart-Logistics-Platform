package com.smartlogistics.reviewservice.service;

import com.smartlogistics.reviewservice.dto.TrustScoreDto;

import java.util.List;
import java.util.UUID;

public interface TrustScoreService {
    TrustScoreDto.TrustScoreResponse getTrustScore(UUID userId);
    List<TrustScoreDto.TrustScoreHistoryResponse> getReputationHistory(UUID userId);
    void updateTrustScore(UUID userId, String reason);
    void handleTripCompleted(UUID userId);
    void handleTripCancelled(UUID userId);
}
