package com.smartlogistics.userservice.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "user-events";

    @Async
    public void publishEvent(String eventType, UUID userId, Object payload) {
        try {
            UserEvent event = UserEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType(eventType)
                    .userId(userId)
                    .payload(payload)
                    .timestamp(Instant.now())
                    .build();
                    
            kafkaTemplate.send(TOPIC, userId.toString(), event);
            log.info("Published {} event for user {}", eventType, userId);
        } catch (Exception e) {
            log.error("Failed to publish {} event for user {} to Kafka: {}", eventType, userId, e.getMessage());
        }
    }
}
