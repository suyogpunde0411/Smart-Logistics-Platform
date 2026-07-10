package com.smartlogistics.notificationservice.events;

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
public class NotificationKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_SENT = "notification.sent";
    private static final String TOPIC_FAILED = "notification.failed";

    public void publishNotificationSent(UUID notificationId, UUID recipientId, String channel, String type) {
        String correlationId = getOrGenerateCorrelationId();
        NotificationSentEvent event = new NotificationSentEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                notificationId,
                recipientId,
                channel,
                type
        );
        send(TOPIC_SENT, notificationId.toString(), event);
    }

    public void publishNotificationFailed(UUID notificationId, UUID recipientId, String channel, String type, String error) {
        String correlationId = getOrGenerateCorrelationId();
        NotificationFailedEvent event = new NotificationFailedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                correlationId,
                notificationId,
                recipientId,
                channel,
                type,
                error
        );
        send(TOPIC_FAILED, notificationId.toString(), event);
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
