package com.smartlogistics.adminservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserVerified(UserVerifiedEvent event) {
        log.info("Publishing UserVerifiedEvent for user: {}", event.userId());
        kafkaTemplate.send("user.verified", event.userId().toString(), event);
    }

    public void publishBusinessVerified(BusinessVerifiedEvent event) {
        log.info("Publishing BusinessVerifiedEvent for business: {}", event.businessId());
        kafkaTemplate.send("business.verified", event.businessId().toString(), event);
    }

    public void publishTruckVerified(TruckVerifiedEvent event) {
        log.info("Publishing TruckVerifiedEvent for truck: {}", event.truckId());
        kafkaTemplate.send("truck.verified", event.truckId().toString(), event);
    }

    public void publishAnnouncementCreated(Object event) {
        log.info("Publishing AnnouncementCreatedEvent");
        kafkaTemplate.send("announcement.created", event);
    }

    public void publishFeatureFlagUpdated(Object event) {
        log.info("Publishing FeatureFlagUpdatedEvent");
        kafkaTemplate.send("feature-flag.updated", event);
    }
}
