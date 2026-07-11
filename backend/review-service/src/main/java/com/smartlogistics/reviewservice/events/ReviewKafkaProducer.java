package com.smartlogistics.reviewservice.events;

import com.smartlogistics.common.event.ReviewCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_CREATED = "review.created";
    private static final String TOPIC_UPDATED = "review.updated";
    private static final String TOPIC_REPORTED = "review.reported";
    private static final String TOPIC_SCORE_UPDATED = "trust-score.updated";

    public void publishReviewCreated(UUID reviewId, UUID tripId, UUID reviewerId, UUID revieweeId, Integer rating, String comment) {
        String correlationId = getOrGenerateCorrelationId();
        ReviewCreatedEvent event = new ReviewCreatedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                reviewId,
                tripId,
                reviewerId,
                revieweeId,
                rating,
                comment
        );
        send(TOPIC_CREATED, reviewId.toString(), event);
    }

    public void publishReviewUpdated(UUID reviewId, UUID revieweeId, Integer rating, String comment) {
        String correlationId = getOrGenerateCorrelationId();
        ReviewUpdatedEvent event = new ReviewUpdatedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                reviewId,
                revieweeId,
                rating,
                comment
        );
        send(TOPIC_UPDATED, reviewId.toString(), event);
    }

    public void publishReviewReported(UUID reviewId, UUID reporterId, String reason) {
        String correlationId = getOrGenerateCorrelationId();
        ReviewReportedEvent event = new ReviewReportedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                reviewId,
                reporterId,
                reason
        );
        send(TOPIC_REPORTED, reviewId.toString(), event);
    }

    public void publishTrustScoreUpdated(UUID userId, Integer oldScore, Integer newScore, String reason) {
        String correlationId = getOrGenerateCorrelationId();
        TrustScoreUpdatedEvent event = new TrustScoreUpdatedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                userId,
                oldScore,
                newScore,
                reason
        );
        send(TOPIC_SCORE_UPDATED, userId.toString(), event);
    }

    private void send(String topic, String key, Object value) {
        log.debug("Sending message to topic: {}, key: {}, payload: {}", topic, key, value);
        kafkaTemplate.send(topic, key, value).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send message to topic: {} key: {} due to: {}", topic, key, ex.getMessage());
            } else {
                log.info("Successfully sent message to topic: {} key: {}", topic, key);
            }
        });
    }

    private String getOrGenerateCorrelationId() {
        String cid = MDC.get("correlationId");
        return cid != null ? cid : UUID.randomUUID().toString();
    }
}
