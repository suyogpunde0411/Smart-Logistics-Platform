package com.smartlogistics.adminservice.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AdminKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AdminKafkaConsumer.class);

    @KafkaListener(topics = "user-registration", groupId = "admin-service-group")
    public void consumeUserRegistration(String message) {
        logger.info("Admin Service consumed user-registration event: {}", message);
    }

    @KafkaListener(topics = "truck.registered", groupId = "admin-service-group")
    public void consumeTruckRegistered(String message) {
        logger.info("Admin Service consumed truck.registered event: {}", message);
    }

    @KafkaListener(topics = "shipment.created", groupId = "admin-service-group")
    public void consumeShipmentCreated(String message) {
        logger.info("Admin Service consumed shipment.created event: {}", message);
    }

    @KafkaListener(topics = "trip.completed", groupId = "admin-service-group")
    public void consumeTripCompleted(String message) {
        logger.info("Admin Service consumed trip.completed event: {}", message);
    }

    @KafkaListener(topics = "review.reported", groupId = "admin-service-group")
    public void consumeReviewReported(String message) {
        logger.info("Admin Service consumed review.reported event: {}", message);
        // Logic to trigger moderation could be added here
    }

    @KafkaListener(topics = "document.uploaded", groupId = "admin-service-group")
    public void consumeDocumentUploaded(String message) {
        logger.info("Admin Service consumed document.uploaded event: {}", message);
        // Logic to create verification requests could be added here
    }
}
