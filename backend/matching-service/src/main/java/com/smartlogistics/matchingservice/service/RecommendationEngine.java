package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.entity.MatchResult;
import java.util.List;
import java.util.UUID;

public interface RecommendationEngine {
    List<MatchResult> generateRecommendations(UUID shipmentId);
}
