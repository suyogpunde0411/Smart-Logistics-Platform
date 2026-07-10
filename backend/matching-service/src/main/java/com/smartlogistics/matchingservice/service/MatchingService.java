package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.dto.*;
import com.smartlogistics.matchingservice.events.ShipmentReadyForMatchingEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MatchingService {
    MatchRequestDto.Response createMatchRequest(MatchRequestDto.CreateRequest request);
    List<MatchResultDto.Response> runManualMatching(UUID shipmentId);
    void processAutomaticMatching(ShipmentReadyForMatchingEvent event);
    void cancelMatchesForShipment(UUID shipmentId, String reason);
    void processTruckAvailabilityChange(UUID truckId, String status);
    void processTruckDeletion(UUID truckId);
    List<MatchResultDto.Response> getRecommendedTrucks(UUID shipmentId);
    List<MatchResultDto.Response> getRecommendedShipments(UUID truckId);
    List<MatchHistoryDto> getMatchHistory();
    Page<MatchResultDto.Response> searchMatches(UUID shipmentId, UUID truckId, UUID driverId, String status, Double minScore, Double maxScore, Pageable pageable);
}
