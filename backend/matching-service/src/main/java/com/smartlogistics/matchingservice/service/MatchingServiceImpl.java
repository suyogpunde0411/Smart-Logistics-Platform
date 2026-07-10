package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.client.ShipmentServiceClient;
import com.smartlogistics.matchingservice.client.ShipmentServiceClient.DetailedShipmentResponse;
import com.smartlogistics.matchingservice.constants.MatchingConstants;
import com.smartlogistics.matchingservice.dto.*;
import com.smartlogistics.matchingservice.entity.*;
import com.smartlogistics.matchingservice.events.MatchingKafkaProducer;
import com.smartlogistics.matchingservice.events.ShipmentReadyForMatchingEvent;
import com.smartlogistics.matchingservice.mapper.MatchMapper;
import com.smartlogistics.matchingservice.repository.*;
import com.smartlogistics.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class MatchingServiceImpl implements MatchingService {

    private final MatchRequestRepository matchRequestRepository;
    private final MatchResultRepository matchResultRepository;
    private final AcceptedMatchRepository acceptedMatchRepository;
    private final RejectedMatchRepository rejectedMatchRepository;
    private final MatchingAuditRepository auditRepository;
    private final RecommendationEngine recommendationEngine;
    private final RecommendationProvider recommendationProvider;
    private final ShipmentServiceClient shipmentServiceClient;
    private final MatchingKafkaProducer kafkaProducer;
    private final MatchMapper mapper;

    @Override
    @Transactional
    public MatchRequestDto.Response createMatchRequest(MatchRequestDto.CreateRequest request) {
        log.info("Creating MatchRequest for shipment: {}", request.shipmentId());

        // Fetch Detailed Shipment
        DetailedShipmentResponse shipment = shipmentServiceClient.getShipmentById(request.shipmentId());
        if (shipment == null) {
            throw new ResourceNotFoundException("Shipment not found with ID: " + request.shipmentId());
        }

        // Build MatchRequest
        int ttl = request.ttlMinutes() != null ? request.ttlMinutes() : 120;
        MatchRequest matchRequest = MatchRequest.builder()
                .shipmentId(request.shipmentId())
                .businessId(shipment.businessOwnerId())
                .status(MatchingConstants.REQ_STATUS_PENDING)
                .pickupLatitude(shipment.originLatitude())
                .pickupLongitude(shipment.originLongitude())
                .destinationLatitude(shipment.destinationLatitude())
                .destinationLongitude(shipment.destinationLongitude())
                .radiusKm(request.radiusKm() != null ? request.radiusKm() : 100.0)
                .maxDistanceKm(request.maxDistanceKm() != null ? request.maxDistanceKm() : 150.0)
                .expiresAt(LocalDateTime.now().plusMinutes(ttl))
                .build();

        MatchRequest savedRequest = matchRequestRepository.save(matchRequest);

        // Audit Log
        MatchingAudit audit = MatchingAudit.builder()
                .action("CREATE_MATCH_REQUEST")
                .shipmentId(savedRequest.getShipmentId())
                .actorId(savedRequest.getBusinessId())
                .details("Created manual match request for shipment.")
                .build();
        auditRepository.save(audit);

        // Trigger matching
        List<MatchResult> results = recommendationEngine.generateRecommendations(savedRequest.getShipmentId());
        for (MatchResult result : results) {
            kafkaProducer.publishMatchCreated(result.getId(), result.getShipmentId(), result.getTruckId(), result.getEstimatedCost());
        }

        return mapper.toDto(savedRequest);
    }

    @Override
    @Transactional
    public List<MatchResultDto.Response> runManualMatching(UUID shipmentId) {
        log.info("Running manual matching for shipment: {}", shipmentId);
        List<MatchResult> results = recommendationEngine.generateRecommendations(shipmentId);

        // Audit Log
        MatchingAudit audit = MatchingAudit.builder()
                .action("MANUAL_MATCHING_RUN")
                .shipmentId(shipmentId)
                .details("Triggered manual matching execution.")
                .build();
        auditRepository.save(audit);

        for (MatchResult result : results) {
            kafkaProducer.publishMatchCreated(result.getId(), result.getShipmentId(), result.getTruckId(), result.getEstimatedCost());
        }

        return results.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void processAutomaticMatching(ShipmentReadyForMatchingEvent event) {
        log.info("Processing automatic matching for shipment: {}", event.shipmentId());

        // Create MatchRequest
        MatchRequest matchRequest = MatchRequest.builder()
                .shipmentId(event.shipmentId())
                .businessId(event.businessOwnerId())
                .status(MatchingConstants.REQ_STATUS_PENDING)
                .pickupLatitude(event.originLatitude())
                .pickupLongitude(event.originLongitude())
                .destinationLatitude(event.destinationLatitude())
                .destinationLongitude(event.destinationLongitude())
                .radiusKm(100.0)
                .maxDistanceKm(150.0)
                .expiresAt(LocalDateTime.now().plusHours(2))
                .build();
        matchRequestRepository.save(matchRequest);

        // Audit Log
        MatchingAudit audit = MatchingAudit.builder()
                .action("AUTO_MATCHING_TRIGGERED")
                .shipmentId(event.shipmentId())
                .actorId(event.businessOwnerId())
                .details("Automated match request created via ready event.")
                .build();
        auditRepository.save(audit);

        // Run recommendations
        List<MatchResult> results = recommendationEngine.generateRecommendations(event.shipmentId());
        for (MatchResult result : results) {
            kafkaProducer.publishMatchCreated(result.getId(), result.getShipmentId(), result.getTruckId(), result.getEstimatedCost());
        }
    }

    @Override
    @Transactional
    public void cancelMatchesForShipment(UUID shipmentId, String reason) {
        log.info("Cancelling all match results for shipment: {}", shipmentId);

        // Expire requests
        matchRequestRepository.findAll().stream()
                .filter(req -> !req.isDeleted() && req.getShipmentId().equals(shipmentId) && MatchingConstants.REQ_STATUS_PENDING.equals(req.getStatus()))
                .forEach(req -> {
                    req.setStatus(MatchingConstants.REQ_STATUS_CANCELLED);
                    matchRequestRepository.save(req);
                });

        // Expire match results
        List<MatchResult> results = matchResultRepository.findTop10ByShipmentIdAndIsDeletedFalseOrderByOverallScoreDesc(shipmentId);
        for (MatchResult res : results) {
            if (MatchingConstants.MATCH_STATUS_RECOMMENDED.equals(res.getStatus())) {
                res.setStatus(MatchingConstants.MATCH_STATUS_REJECTED);
                matchResultRepository.save(res);
            }
        }

        // Audit Log
        MatchingAudit audit = MatchingAudit.builder()
                .action("CANCEL_SHIPMENT_MATCHES")
                .shipmentId(shipmentId)
                .details("Cancelled matching due to shipment cancellation event. Reason: " + reason)
                .build();
        auditRepository.save(audit);
    }

    @Override
    @Transactional
    public void processTruckAvailabilityChange(UUID truckId, String status) {
        log.info("Processing availability changed to {} for truck: {}", status, truckId);
        if (!"AVAILABLE".equalsIgnoreCase(status) && !"ACTIVE".equalsIgnoreCase(status)) {
            // Expire recommended match results involving this truck
            List<MatchResult> results = matchResultRepository.findTop10ByTruckIdAndIsDeletedFalseOrderByOverallScoreDesc(truckId);
            for (MatchResult res : results) {
                if (MatchingConstants.MATCH_STATUS_RECOMMENDED.equals(res.getStatus())) {
                    res.setStatus(MatchingConstants.MATCH_STATUS_EXPIRED);
                    matchResultRepository.save(res);
                }
            }
        }
    }

    @Override
    @Transactional
    public void processTruckDeletion(UUID truckId) {
        log.info("Processing truck deletion for truck: {}", truckId);
        List<MatchResult> results = matchResultRepository.findTop10ByTruckIdAndIsDeletedFalseOrderByOverallScoreDesc(truckId);
        for (MatchResult res : results) {
            if (MatchingConstants.MATCH_STATUS_RECOMMENDED.equals(res.getStatus())) {
                res.setStatus(MatchingConstants.MATCH_STATUS_EXPIRED);
                matchResultRepository.save(res);
            }
        }
    }

    @Override
    public List<MatchResultDto.Response> getRecommendedTrucks(UUID shipmentId) {
        return recommendationProvider.getRecommendedTrucks(shipmentId, 10)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MatchResultDto.Response> getRecommendedShipments(UUID truckId) {
        return recommendationProvider.getRecommendedShipments(truckId, 10)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MatchHistoryDto> getMatchHistory() {
        List<MatchHistoryDto> history = new ArrayList<>();

        // Add Accepted Matches
        List<AcceptedMatch> accepted = acceptedMatchRepository.findAll();
        for (AcceptedMatch am : accepted) {
            if (!am.isDeleted()) {
                history.add(new MatchHistoryDto(
                        am.getMatchResult().getId(),
                        am.getMatchResult().getShipmentId(),
                        am.getMatchResult().getTruckId(),
                        am.getMatchResult().getDriverId(),
                        am.getMatchResult().getMatchRequest().getBusinessId(),
                        am.getMatchResult().getOverallScore(),
                        am.getBid() != null ? am.getBid().getAmount() : am.getMatchResult().getEstimatedCost(),
                        MatchingConstants.MATCH_STATUS_ACCEPTED,
                        am.getAcceptedAt(),
                        "Match was accepted by business."
                ));
            }
        }

        // Add Rejected Matches
        List<RejectedMatch> rejected = rejectedMatchRepository.findAll();
        for (RejectedMatch rm : rejected) {
            if (!rm.isDeleted()) {
                history.add(new MatchHistoryDto(
                        rm.getMatchResult().getId(),
                        rm.getMatchResult().getShipmentId(),
                        rm.getMatchResult().getTruckId(),
                        rm.getMatchResult().getDriverId(),
                        rm.getMatchResult().getMatchRequest().getBusinessId(),
                        rm.getMatchResult().getOverallScore(),
                        rm.getBid() != null ? rm.getBid().getAmount() : rm.getMatchResult().getEstimatedCost(),
                        MatchingConstants.MATCH_STATUS_REJECTED,
                        rm.getRejectedAt(),
                        "Match was rejected by business. Reason: " + rm.getReason()
                ));
            }
        }

        history.sort(Comparator.comparing(MatchHistoryDto::timestamp).reversed());
        return history;
    }

    @Override
    public Page<MatchResultDto.Response> searchMatches(UUID shipmentId, UUID truckId, UUID driverId, String status, Double minScore, Double maxScore, Pageable pageable) {
        return matchResultRepository.search(shipmentId, truckId, driverId, status, minScore, maxScore, pageable)
                .map(mapper::toDto);
    }
}
