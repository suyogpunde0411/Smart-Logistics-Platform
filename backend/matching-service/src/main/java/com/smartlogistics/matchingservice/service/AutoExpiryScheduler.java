package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.constants.MatchingConstants;
import com.smartlogistics.matchingservice.entity.Bid;
import com.smartlogistics.matchingservice.entity.BidHistory;
import com.smartlogistics.matchingservice.entity.MatchResult;
import com.smartlogistics.matchingservice.entity.MatchingAudit;
import com.smartlogistics.matchingservice.events.MatchingKafkaProducer;
import com.smartlogistics.matchingservice.repository.BidHistoryRepository;
import com.smartlogistics.matchingservice.repository.BidRepository;
import com.smartlogistics.matchingservice.repository.MatchResultRepository;
import com.smartlogistics.matchingservice.repository.MatchingAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoExpiryScheduler {

    private final BidRepository bidRepository;
    private final MatchResultRepository matchResultRepository;
    private final BidHistoryRepository bidHistoryRepository;
    private final MatchingAuditRepository auditRepository;
    private final MatchingKafkaProducer kafkaProducer;

    @Scheduled(fixedDelayString = "${matching.match-expiry-scan-delay-ms:300000}")
    @Transactional
    public void scanAndExpireMatches() {
        log.debug("Scanning for expired match results...");
        LocalDateTime now = LocalDateTime.now();
        List<MatchResult> expiredMatches = matchResultRepository.findByStatusAndExpiresAtBeforeAndIsDeletedFalse(
                MatchingConstants.MATCH_STATUS_RECOMMENDED, now);

        for (MatchResult match : expiredMatches) {
            match.setStatus(MatchingConstants.MATCH_STATUS_EXPIRED);
            matchResultRepository.save(match);

            // Audit Log
            MatchingAudit audit = MatchingAudit.builder()
                    .action("AUTO_EXPIRE_MATCH")
                    .shipmentId(match.getShipmentId())
                    .truckId(match.getTruckId())
                    .details("Match result expired automatically.")
                    .build();
            auditRepository.save(audit);

            // Kafka Event
            kafkaProducer.publishMatchExpired(match.getId(), match.getShipmentId(), match.getTruckId());
            log.info("Expired MatchResult: {} for shipment: {} and truck: {}", match.getId(), match.getShipmentId(), match.getTruckId());
        }
    }

    @Scheduled(fixedDelayString = "${matching.bid-expiry-scan-delay-ms:300000}")
    @Transactional
    public void scanAndExpireBids() {
        log.debug("Scanning for expired bids...");
        LocalDateTime now = LocalDateTime.now();
        List<Bid> expiredBids = bidRepository.findByStatusAndExpiresAtBeforeAndIsDeletedFalse(
                MatchingConstants.BID_STATUS_PENDING, now);

        for (Bid bid : expiredBids) {
            String oldStatus = bid.getStatus();
            bid.setStatus(MatchingConstants.BID_STATUS_EXPIRED);
            bidRepository.save(bid);

            // Bid History
            BidHistory history = BidHistory.builder()
                    .bid(bid)
                    .oldStatus(oldStatus)
                    .newStatus(MatchingConstants.BID_STATUS_EXPIRED)
                    .changedAt(now)
                    .remarks("Bid expired automatically.")
                    .build();
            bidHistoryRepository.save(history);

            // Audit Log
            MatchingAudit audit = MatchingAudit.builder()
                    .action("AUTO_EXPIRE_BID")
                    .shipmentId(bid.getShipmentId())
                    .truckId(bid.getTruckId())
                    .actorId(bid.getDriverId())
                    .details("Bid expired automatically.")
                    .build();
            auditRepository.save(audit);

            // Kafka Event
            kafkaProducer.publishBidRejected(bid.getId(), bid.getShipmentId(), bid.getTruckId(), bid.getDriverId(), "Expired automatically");
            log.info("Expired Bid: {} for shipment: {} by driver: {}", bid.getId(), bid.getShipmentId(), bid.getDriverId());
        }
    }
}
