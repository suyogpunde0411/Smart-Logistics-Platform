package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.entity.MatchResult;
import java.util.List;
import java.util.UUID;

public interface RecommendationProvider {
    List<MatchResult> getRecommendedTrucks(UUID shipmentId, int limit);
    List<MatchResult> getRecommendedShipments(UUID truckId, int limit);
}
