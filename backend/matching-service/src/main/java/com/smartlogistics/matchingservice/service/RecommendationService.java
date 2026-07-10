package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.client.ShipmentServiceClient;
import com.smartlogistics.matchingservice.client.ShipmentServiceClient.DetailedShipmentResponse;
import com.smartlogistics.matchingservice.client.TruckServiceClient;
import com.smartlogistics.matchingservice.constants.MatchingConstants;
import com.smartlogistics.matchingservice.dto.CustomPage;
import com.smartlogistics.matchingservice.dto.MatchResultDto;
import com.smartlogistics.matchingservice.entity.MatchRequest;
import com.smartlogistics.matchingservice.entity.MatchResult;
import com.smartlogistics.matchingservice.entity.RecommendationLog;
import com.smartlogistics.matchingservice.repository.MatchRequestRepository;
import com.smartlogistics.matchingservice.repository.MatchResultRepository;
import com.smartlogistics.matchingservice.repository.RecommendationLogRepository;
import com.smartlogistics.matchingservice.service.strategy.MatchingStrategy;
import com.smartlogistics.shared.dto.TruckDTO;
import com.smartlogistics.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService implements RecommendationProvider, RecommendationEngine {

    private final MatchResultRepository matchResultRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final RecommendationLogRepository recommendationLogRepository;
    private final ShipmentServiceClient shipmentServiceClient;
    private final TruckServiceClient truckServiceClient;
    private final MatchingStrategy matchingStrategy;

    @Override
    @Transactional
    public List<MatchResult> generateRecommendations(UUID shipmentId) {
        log.info("Generating recommendations for shipment: {}", shipmentId);

        // Fetch Detailed Shipment
        DetailedShipmentResponse shipment;
        try {
            shipment = shipmentServiceClient.getShipmentById(shipmentId);
        } catch (Exception e) {
            log.error("Failed to fetch shipment metadata for ID: {}", shipmentId, e);
            throw new ResourceNotFoundException("Shipment not found with ID: " + shipmentId);
        }

        // Validate Shipment Availability
        if (shipment == null) {
            throw new ResourceNotFoundException("Shipment not found with ID: " + shipmentId);
        }
        if (!"AVAILABLE".equalsIgnoreCase(shipment.status()) && !"CREATED".equalsIgnoreCase(shipment.status())) {
            log.warn("Shipment {} has status {}, skipping recommendation generation.", shipmentId, shipment.status());
            return List.of();
        }

        // Fetch or create MatchRequest
        MatchRequest matchRequest = matchRequestRepository.findAll().stream()
                .filter(req -> !req.isDeleted() && req.getShipmentId().equals(shipmentId) && MatchingConstants.REQ_STATUS_PENDING.equals(req.getStatus()))
                .findFirst()
                .orElseGet(() -> {
                    MatchRequest newReq = MatchRequest.builder()
                            .shipmentId(shipmentId)
                            .businessId(shipment.businessOwnerId())
                            .status(MatchingConstants.REQ_STATUS_PENDING)
                            .pickupLatitude(shipment.originLatitude())
                            .pickupLongitude(shipment.originLongitude())
                            .destinationLatitude(shipment.destinationLatitude())
                            .destinationLongitude(shipment.destinationLongitude())
                            .radiusKm(100.0)
                            .maxDistanceKm(150.0)
                            .expiresAt(LocalDateTime.now().plusHours(2))
                            .build();
                    return matchRequestRepository.save(newReq);
                });

        double radius = matchRequest.getRadiusKm() != null ? matchRequest.getRadiusKm() : 100.0;
        double originLat = shipment.originLatitude() != null ? shipment.originLatitude() : 0.0;
        double originLng = shipment.originLongitude() != null ? shipment.originLongitude() : 0.0;

        // Fetch Available/Nearby Trucks
        List<TruckDTO.Response> trucks = new ArrayList<>();
        try {
            CustomPage<TruckDTO.Response> nearbyPage = truckServiceClient.findNearbyTrucks(originLat, originLng, radius, 0, 50);
            if (nearbyPage != null && nearbyPage.content() != null) {
                trucks.addAll(nearbyPage.content());
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve nearby trucks from truck-service, attempting global search", e);
            try {
                CustomPage<TruckDTO.Response> searchPage = truckServiceClient.searchTrucks(
                        null, null, "AVAILABLE", null, true, shipment.totalWeight(), shipment.totalVolume(), 0, 50);
                if (searchPage != null && searchPage.content() != null) {
                    trucks.addAll(searchPage.content());
                }
            } catch (Exception ex) {
                log.error("Could not fetch any trucks for matching", ex);
            }
        }

        log.info("Found {} candidate trucks for matching.", trucks.size());
        List<MatchResult> recommendations = new ArrayList<>();

        for (TruckDTO.Response truck : trucks) {
            try {
                // If match already exists for this shipment and truck, skip creating a duplicate.
                if (matchResultRepository.existsByShipmentIdAndTruckIdAndIsDeletedFalse(shipmentId, truck.id())) {
                    continue;
                }

                MatchResultDto.ScoreDetails scoreDetails = matchingStrategy.calculateScore(matchRequest, shipment, truck);
                if (scoreDetails.overallScore() > 0.0) {
                    MatchResult result = MatchResult.builder()
                            .matchRequest(matchRequest)
                            .shipmentId(shipmentId)
                            .truckId(truck.id())
                            .driverId(truck.ownerId()) // Assuming truck owner acts as driver
                            .overallScore(scoreDetails.overallScore())
                            .distanceScore(scoreDetails.distanceScore())
                            .capacityScore(scoreDetails.capacityScore())
                            .availabilityScore(scoreDetails.availabilityScore())
                            .compatibilityScore(scoreDetails.compatibilityScore())
                            .estimatedEtaMinutes(scoreDetails.estimatedEtaMinutes())
                            .estimatedCost(scoreDetails.estimatedCost())
                            .reasoning(scoreDetails.reasoning())
                            .status(MatchingConstants.MATCH_STATUS_RECOMMENDED)
                            .expiresAt(matchRequest.getExpiresAt())
                            .build();

                    recommendations.add(result);
                }
            } catch (Exception e) {
                log.error("Error scoring truck {} against shipment {}", truck.id(), shipmentId, e);
            }
        }

        // Save top recommendations
        recommendations.sort(Comparator.comparing(MatchResult::getOverallScore).reversed());
        List<MatchResult> topRecommendations = recommendations.stream().limit(10).collect(Collectors.toList());
        List<MatchResult> savedResults = matchResultRepository.saveAll(topRecommendations);

        // Logging the recommendation run
        RecommendationLog logEntry = RecommendationLog.builder()
                .recommendationType("AUTOMATIC")
                .shipmentId(shipmentId)
                .resultCount(savedResults.size())
                .provider("RuleBasedMatchingStrategy")
                .build();
        recommendationLogRepository.save(logEntry);

        log.info("Generated and stored {} top match results for shipment: {}", savedResults.size(), shipmentId);
        return savedResults;
    }

    @Override
    public List<MatchResult> getRecommendedTrucks(UUID shipmentId, int limit) {
        return matchResultRepository.findTop10ByShipmentIdAndIsDeletedFalseOrderByOverallScoreDesc(shipmentId)
                .stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public List<MatchResult> getRecommendedShipments(UUID truckId, int limit) {
        return matchResultRepository.findTop10ByTruckIdAndIsDeletedFalseOrderByOverallScoreDesc(truckId)
                .stream().limit(limit).collect(Collectors.toList());
    }
}
