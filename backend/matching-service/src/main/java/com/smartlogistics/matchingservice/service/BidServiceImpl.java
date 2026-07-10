package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.client.ShipmentServiceClient;
import com.smartlogistics.matchingservice.client.ShipmentServiceClient.DetailedShipmentResponse;
import com.smartlogistics.matchingservice.constants.MatchingConstants;
import com.smartlogistics.matchingservice.dto.BidDto;
import com.smartlogistics.matchingservice.entity.*;
import com.smartlogistics.matchingservice.events.MatchingKafkaProducer;
import com.smartlogistics.matchingservice.exception.*;
import com.smartlogistics.matchingservice.mapper.MatchMapper;
import com.smartlogistics.matchingservice.repository.*;
import com.smartlogistics.shared.exception.ResourceNotFoundException;
import com.smartlogistics.shared.security.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final MatchResultRepository matchResultRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final AcceptedMatchRepository acceptedMatchRepository;
    private final RejectedMatchRepository rejectedMatchRepository;
    private final BidHistoryRepository bidHistoryRepository;
    private final MatchingAuditRepository auditRepository;
    private final ShipmentServiceClient shipmentServiceClient;
    private final MatchingKafkaProducer kafkaProducer;
    private final MatchMapper mapper;

    @Value("${matching.default-bid-ttl-minutes:60}")
    private int defaultBidTtlMinutes;

    @Override
    @Transactional
    public BidDto.Response placeBid(BidDto.CreateRequest request) {
        log.info("Placing bid on match result: {} for amount: {}", request.matchResultId(), request.amount());

        // Extract Current Driver ID
        UUID driverId = CurrentUserUtil.getUserId();
        if (driverId == null) {
            throw new InvalidMatchException("Authenticated user context is missing.");
        }

        // Fetch and validate MatchResult
        MatchResult matchResult = matchResultRepository.findByIdAndIsDeletedFalse(request.matchResultId())
                .orElseThrow(() -> new MatchNotFoundException("MatchResult not found: " + request.matchResultId()));

        if (!MatchingConstants.MATCH_STATUS_RECOMMENDED.equals(matchResult.getStatus())) {
            throw new InvalidMatchException("Bids can only be placed on RECOMMENDED matches.");
        }

        // Check if shipment is still available
        DetailedShipmentResponse shipment = shipmentServiceClient.getShipmentById(matchResult.getShipmentId());
        if (shipment == null || (!"AVAILABLE".equalsIgnoreCase(shipment.status()) && !"CREATED".equalsIgnoreCase(shipment.status()))) {
            throw new ShipmentUnavailableException("Shipment is no longer available for bidding.");
        }

        // Prevent Duplicate Bids
        if (bidRepository.existsByMatchResultIdAndDriverIdAndIsDeletedFalse(request.matchResultId(), driverId)) {
            throw new DuplicateBidException("Driver has already placed a bid on this match.");
        }

        int ttl = request.ttlMinutes() != null ? request.ttlMinutes() : defaultBidTtlMinutes;
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(ttl);

        // Build Bid
        Bid bid = Bid.builder()
                .matchResult(matchResult)
                .shipmentId(matchResult.getShipmentId())
                .truckId(matchResult.getTruckId())
                .driverId(driverId)
                .businessId(matchResult.getMatchRequest().getBusinessId())
                .amount(request.amount())
                .currency(request.currency())
                .status(MatchingConstants.BID_STATUS_PENDING)
                .message(request.message())
                .expiresAt(expiresAt)
                .build();

        Bid savedBid = bidRepository.save(bid);

        // Bid History
        BidHistory history = BidHistory.builder()
                .bid(savedBid)
                .oldStatus(null)
                .newStatus(MatchingConstants.BID_STATUS_PENDING)
                .changedAt(LocalDateTime.now())
                .remarks("Bid placed successfully.")
                .build();
        bidHistoryRepository.save(history);

        // Audit Log
        MatchingAudit audit = MatchingAudit.builder()
                .action("PLACE_BID")
                .shipmentId(savedBid.getShipmentId())
                .truckId(savedBid.getTruckId())
                .actorId(driverId)
                .details(String.format("Placed bid of %.2f %s on match.", request.amount(), request.currency()))
                .build();
        auditRepository.save(audit);

        // Kafka Event
        kafkaProducer.publishBidPlaced(savedBid.getId(), savedBid.getShipmentId(), driverId, request.amount());

        return mapper.toDto(savedBid);
    }

    @Override
    @Transactional
    public BidDto.Response acceptBid(UUID bidId) {
        log.info("Accepting bid: {}", bidId);

        // Fetch and validate Bid
        Bid bid = bidRepository.findByIdAndIsDeletedFalse(bidId)
                .orElseThrow(() -> new BidNotFoundException("Bid not found: " + bidId));

        if (!MatchingConstants.BID_STATUS_PENDING.equals(bid.getStatus())) {
            throw new InvalidMatchException("Only PENDING bids can be accepted.");
        }

        // Verify Shipment Status
        DetailedShipmentResponse shipment = shipmentServiceClient.getShipmentById(bid.getShipmentId());
        if (shipment == null || (!"AVAILABLE".equalsIgnoreCase(shipment.status()) && !"CREATED".equalsIgnoreCase(shipment.status()))) {
            throw new ShipmentUnavailableException("Shipment is no longer available.");
        }

        LocalDateTime now = LocalDateTime.now();

        // 1. Update Accepted Bid Status
        String oldBidStatus = bid.getStatus();
        bid.setStatus(MatchingConstants.BID_STATUS_ACCEPTED);
        Bid savedBid = bidRepository.save(bid);

        // Bid History for Accepted Bid
        BidHistory history = BidHistory.builder()
                .bid(savedBid)
                .oldStatus(oldBidStatus)
                .newStatus(MatchingConstants.BID_STATUS_ACCEPTED)
                .changedAt(now)
                .remarks("Bid accepted by business.")
                .build();
        bidHistoryRepository.save(history);

        // 2. Update MatchResult Status
        MatchResult matchResult = bid.getMatchResult();
        matchResult.setStatus(MatchingConstants.MATCH_STATUS_ACCEPTED);
        matchResultRepository.save(matchResult);

        // 3. Update MatchRequest Status
        MatchRequest matchRequest = matchResult.getMatchRequest();
        matchRequest.setStatus(MatchingConstants.REQ_STATUS_COMPLETED);
        matchRequestRepository.save(matchRequest);

        // 4. Create AcceptedMatch Entity
        AcceptedMatch acceptedMatch = AcceptedMatch.builder()
                .matchResult(matchResult)
                .bid(savedBid)
                .acceptedAt(now)
                .build();
        acceptedMatchRepository.save(acceptedMatch);

        // 5. Auto-Reject other bids for this shipment
        List<Bid> allBids = bidRepository.findAll().stream()
                .filter(b -> !b.isDeleted() && b.getShipmentId().equals(bid.getShipmentId()) && !b.getId().equals(bidId))
                .toList();
        for (Bid otherBid : allBids) {
            if (MatchingConstants.BID_STATUS_PENDING.equals(otherBid.getStatus())) {
                String oldStatus = otherBid.getStatus();
                otherBid.setStatus(MatchingConstants.BID_STATUS_REJECTED);
                bidRepository.save(otherBid);

                BidHistory bh = BidHistory.builder()
                        .bid(otherBid)
                        .oldStatus(oldStatus)
                        .newStatus(MatchingConstants.BID_STATUS_REJECTED)
                        .changedAt(now)
                        .remarks("Automatically rejected because another bid was accepted.")
                        .build();
                bidHistoryRepository.save(bh);

                RejectedMatch rm = RejectedMatch.builder()
                        .matchResult(otherBid.getMatchResult())
                        .bid(otherBid)
                        .rejectedAt(now)
                        .reason("Automatically rejected due to acceptance of bid: " + bidId)
                        .build();
                rejectedMatchRepository.save(rm);

                kafkaProducer.publishBidRejected(otherBid.getId(), otherBid.getShipmentId(), otherBid.getTruckId(), otherBid.getDriverId(), "Another bid was accepted.");
            }
        }

        // 6. Invalidate other recommended matches for this shipment
        List<MatchResult> allMatches = matchResultRepository.findTop10ByShipmentIdAndIsDeletedFalseOrderByOverallScoreDesc(bid.getShipmentId());
        for (MatchResult otherMatch : allMatches) {
            if (!otherMatch.getId().equals(matchResult.getId()) && MatchingConstants.MATCH_STATUS_RECOMMENDED.equals(otherMatch.getStatus())) {
                otherMatch.setStatus(MatchingConstants.MATCH_STATUS_REJECTED);
                matchResultRepository.save(otherMatch);
            }
        }

        // Audit Log
        MatchingAudit audit = MatchingAudit.builder()
                .action("ACCEPT_BID")
                .shipmentId(savedBid.getShipmentId())
                .truckId(savedBid.getTruckId())
                .actorId(CurrentUserUtil.getUserId())
                .details(String.format("Accepted bid %s for amount %.2f %s.", bidId, savedBid.getAmount(), savedBid.getCurrency()))
                .build();
        auditRepository.save(audit);

        // Kafka Event
        kafkaProducer.publishBidAccepted(matchResult.getId(), savedBid.getShipmentId(), savedBid.getDriverId(), savedBid.getTruckId(), savedBid.getAmount());

        return mapper.toDto(savedBid);
    }

    @Override
    @Transactional
    public BidDto.Response rejectBid(UUID bidId, BidDto.RejectRequest request) {
        log.info("Rejecting bid: {}", bidId);

        // Fetch and validate Bid
        Bid bid = bidRepository.findByIdAndIsDeletedFalse(bidId)
                .orElseThrow(() -> new BidNotFoundException("Bid not found: " + bidId));

        if (!MatchingConstants.BID_STATUS_PENDING.equals(bid.getStatus())) {
            throw new InvalidMatchException("Only PENDING bids can be rejected.");
        }

        LocalDateTime now = LocalDateTime.now();

        // Update Bid Status
        String oldBidStatus = bid.getStatus();
        bid.setStatus(MatchingConstants.BID_STATUS_REJECTED);
        Bid savedBid = bidRepository.save(bid);

        // Bid History
        BidHistory history = BidHistory.builder()
                .bid(savedBid)
                .oldStatus(oldBidStatus)
                .newStatus(MatchingConstants.BID_STATUS_REJECTED)
                .changedAt(now)
                .remarks("Bid rejected: " + request.reason())
                .build();
        bidHistoryRepository.save(history);

        // Create RejectedMatch Record
        RejectedMatch rejectedMatch = RejectedMatch.builder()
                .matchResult(bid.getMatchResult())
                .bid(savedBid)
                .rejectedAt(now)
                .reason(request.reason())
                .build();
        rejectedMatchRepository.save(rejectedMatch);

        // Audit Log
        MatchingAudit audit = MatchingAudit.builder()
                .action("REJECT_BID")
                .shipmentId(savedBid.getShipmentId())
                .truckId(savedBid.getTruckId())
                .actorId(CurrentUserUtil.getUserId())
                .details(String.format("Rejected bid %s. Reason: %s", bidId, request.reason()))
                .build();
        auditRepository.save(audit);

        // Kafka Event
        kafkaProducer.publishBidRejected(savedBid.getId(), savedBid.getShipmentId(), savedBid.getTruckId(), savedBid.getDriverId(), request.reason());

        return mapper.toDto(savedBid);
    }

    @Override
    @Transactional
    public void completeMatchWorkflow(UUID shipmentId, UUID truckId) {
        log.info("Completing match workflow for shipment: {} and truck: {}", shipmentId, truckId);

        AcceptedMatch am = acceptedMatchRepository.findAll().stream()
                .filter(m -> !m.isDeleted() && m.getMatchResult().getShipmentId().equals(shipmentId) && m.getMatchResult().getTruckId().equals(truckId))
                .findFirst()
                .orElse(null);

        if (am == null) {
            log.warn("No accepted match found for shipment {} and truck {}. Cannot complete.", shipmentId, truckId);
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        // Complete MatchResult status
        MatchResult mr = am.getMatchResult();
        mr.setStatus(MatchingConstants.MATCH_STATUS_COMPLETED);
        matchResultRepository.save(mr);

        // Complete Bid status
        Bid bid = am.getBid();
        if (bid != null) {
            String oldStatus = bid.getStatus();
            bid.setStatus(MatchingConstants.BID_STATUS_COMPLETED);
            bidRepository.save(bid);

            BidHistory bh = BidHistory.builder()
                    .bid(bid)
                    .oldStatus(oldStatus)
                    .newStatus(MatchingConstants.BID_STATUS_COMPLETED)
                    .changedAt(now)
                    .remarks("Match completed via trip completion.")
                    .build();
            bidHistoryRepository.save(bh);
        }

        // Audit Log
        MatchingAudit audit = MatchingAudit.builder()
                .action("COMPLETE_MATCH_WORKFLOW")
                .shipmentId(shipmentId)
                .truckId(truckId)
                .details("Completed match workflow successfully upon trip completion.")
                .build();
        auditRepository.save(audit);

        // Kafka Event
        kafkaProducer.publishMatchCompleted(mr.getId(), shipmentId, truckId, mr.getDriverId());
        log.info("Completed matching workflow. Published MatchCompletedEvent.");
    }
}
