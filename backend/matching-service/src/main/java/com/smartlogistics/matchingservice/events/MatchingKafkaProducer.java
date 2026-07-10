package com.smartlogistics.matchingservice.events;

import com.smartlogistics.common.event.BidCreatedEvent;
import com.smartlogistics.common.event.MatchAcceptedEvent;
import com.smartlogistics.shared.event.MatchCreatedEvent;
import com.smartlogistics.matchingservice.constants.MatchingConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishMatchCreated(UUID matchId, UUID shipmentId, UUID truckId, Double price) {
        String correlationId = UUID.randomUUID().toString();
        MatchCreatedEvent event = new MatchCreatedEvent(matchId, shipmentId, truckId, price, correlationId);
        sendSafely(MatchingConstants.TOPIC_MATCH_CREATED, matchId.toString(), event, "MatchCreated");
    }

    public void publishBidPlaced(UUID bidId, UUID shipmentId, UUID driverId, Double amount) {
        String correlationId = UUID.randomUUID().toString();
        BidCreatedEvent event = new BidCreatedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                bidId,
                shipmentId,
                driverId,
                amount
        );
        sendSafely(MatchingConstants.TOPIC_BID_PLACED, bidId.toString(), event, "BidPlaced");
    }

    public void publishBidAccepted(UUID matchId, UUID shipmentId, UUID driverId, UUID truckId, Double price) {
        String correlationId = UUID.randomUUID().toString();
        MatchAcceptedEvent event = new MatchAcceptedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                matchId,
                shipmentId,
                driverId,
                truckId,
                price
        );
        sendSafely(MatchingConstants.TOPIC_BID_ACCEPTED, matchId.toString(), event, "BidAccepted");
    }

    public void publishBidRejected(UUID bidId, UUID shipmentId, UUID truckId, UUID driverId, String reason) {
        String correlationId = UUID.randomUUID().toString();
        BidRejectedEvent event = new BidRejectedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                bidId,
                shipmentId,
                truckId,
                driverId,
                reason
        );
        sendSafely(MatchingConstants.TOPIC_BID_REJECTED, bidId.toString(), event, "BidRejected");
    }

    public void publishMatchExpired(UUID matchId, UUID shipmentId, UUID truckId) {
        String correlationId = UUID.randomUUID().toString();
        MatchExpiredEvent event = new MatchExpiredEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                matchId,
                shipmentId,
                truckId
        );
        sendSafely(MatchingConstants.TOPIC_MATCH_EXPIRED, matchId.toString(), event, "MatchExpired");
    }

    public void publishMatchCompleted(UUID matchId, UUID shipmentId, UUID truckId, UUID driverId) {
        String correlationId = UUID.randomUUID().toString();
        MatchCompletedEvent event = new MatchCompletedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                matchId,
                shipmentId,
                truckId,
                driverId
        );
        sendSafely(MatchingConstants.TOPIC_MATCH_COMPLETED, matchId.toString(), event, "MatchCompleted");
    }

    private void sendSafely(String topic, String key, Object payload, String eventName) {
        try {
            kafkaTemplate.send(topic, key, payload);
            log.info("Successfully published {} to topic '{}' with key: {}", eventName, topic, key);
        } catch (Exception e) {
            log.error("Failed to publish {} to topic '{}' with key: {}. Error: {}", eventName, topic, key, e.getMessage(), e);
        }
    }
}
