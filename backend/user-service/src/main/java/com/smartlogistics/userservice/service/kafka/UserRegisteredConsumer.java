package com.smartlogistics.userservice.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.userservice.dto.UserRegisteredEvent;
import com.smartlogistics.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegisteredConsumer {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user-registration", groupId = "user-service-group")
    public void consumeUserRegistration(String message) {
        log.info("Received user registration event from Kafka: {}", message);
        try {
            UserRegisteredEvent event = objectMapper.readValue(message, UserRegisteredEvent.class);
            userService.createProfile(event);
            log.info("Successfully synced user registration event for: {}", event.email());
        } catch (Exception e) {
            log.error("Error processing user registration event: {}", e.getMessage(), e);
        }
    }
}
