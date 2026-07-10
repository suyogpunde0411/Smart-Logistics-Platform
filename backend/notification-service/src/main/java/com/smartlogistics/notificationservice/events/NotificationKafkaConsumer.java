package com.smartlogistics.notificationservice.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.common.client.ShipmentFeignClient;
import com.smartlogistics.common.event.*;
import com.smartlogistics.shared.event.MatchCreatedEvent;
import com.smartlogistics.notificationservice.entity.NotificationTemplate;
import com.smartlogistics.notificationservice.exception.TemplateNotFoundException;
import com.smartlogistics.notificationservice.repository.NotificationTemplateRepository;
import com.smartlogistics.notificationservice.service.NotificationService;
import com.smartlogistics.notificationservice.util.TemplateEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaConsumer {

    private final NotificationService notificationService;
    private final NotificationTemplateRepository templateRepository;
    private final ShipmentFeignClient shipmentFeignClient;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user-registration", groupId = "notification-service-group")
    public void consumeUserRegistered(UserRegisteredEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming UserRegisteredEvent for user: {}", event.userId());
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("firstName", event.email().split("@")[0]);
            variables.put("code", "123456");
            variables.put("link", "http://localhost:8080/verify");

            processNotification(event.userId(), "Welcome Email", variables);
            processNotification(event.userId(), "Email Verification", variables);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "user-events", groupId = "notification-service-group")
    public void consumeUserEvent(String message) {
        log.info("Consuming raw user-event: {}", message);
        try {
            Map<String, Object> eventMap = objectMapper.readValue(message, Map.class);
            String correlationId = (String) eventMap.get("correlationId");
            String eventId = (String) eventMap.get("eventId");
            setupMdc(correlationId, eventId);

            String eventType = (String) eventMap.get("eventType");
            String userIdStr = (String) eventMap.get("userId");
            if (userIdStr == null) return;
            UUID userId = UUID.fromString(userIdStr);

            if ("USER_PROFILE_COMPLETED".equals(eventType) || "DRIVER_PROFILE_COMPLETED".equals(eventType) || "BUSINESS_PROFILE_COMPLETED".equals(eventType)) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("firstName", "User");
                processNotification(userId, "Welcome Email", variables);
            }
        } catch (Exception e) {
            log.error("Error processing user-event message: {}", e.getMessage());
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "truck.registered", groupId = "notification-service-group")
    public void consumeTruckRegistered(TruckRegisteredEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming TruckRegisteredEvent for truck: {}", event.truckId());
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("licensePlate", event.licensePlate());
            variables.put("capacity", event.capacity());

            processNotification(event.ownerId(), "Truck Registered", variables);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "shipment.created", groupId = "notification-service-group")
    public void consumeShipmentCreated(ShipmentCreatedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming ShipmentCreatedEvent for shipment: {}", event.shipmentId());
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("shipmentId", event.shipmentId());
            variables.put("origin", event.origin());
            variables.put("destination", event.destination());

            processNotification(event.businessId(), "Shipment Created", variables);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "match.created", groupId = "notification-service-group")
    public void consumeMatchCreated(MatchCreatedEvent event) {
        setupMdc(event.getCorrelationId(), event.getEventId());
        log.info("Consuming MatchCreatedEvent for match: {}", event.getMatchId());
        try {
            UUID businessId = fetchBusinessOwnerId(event.getShipmentId());
            if (businessId != null) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("shipmentId", event.getShipmentId());
                variables.put("truckId", event.getTruckId());

                processNotification(businessId, "Shipment Matched", variables);
            }
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "bid.placed", groupId = "notification-service-group")
    public void consumeBidPlaced(BidCreatedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming BidCreatedEvent (Bid Placed) for bid: {}", event.bidId());
        try {
            UUID businessId = fetchBusinessOwnerId(event.shipmentId());
            if (businessId != null) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("price", event.amount());
                variables.put("shipmentId", event.shipmentId());

                processNotification(businessId, "Bid Received", variables);
            }
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "bid.accepted", groupId = "notification-service-group")
    public void consumeBidAccepted(MatchAcceptedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming MatchAcceptedEvent (Bid Accepted) for match: {}", event.matchId());
        try {
            // 1. Notify Driver
            Map<String, Object> driverVars = new HashMap<>();
            driverVars.put("price", event.price());
            driverVars.put("shipmentId", event.shipmentId());
            processNotification(event.driverId(), "Bid Accepted", driverVars);

            // 2. Notify Business Owner
            UUID businessId = fetchBusinessOwnerId(event.shipmentId());
            if (businessId != null) {
                Map<String, Object> businessVars = new HashMap<>();
                businessVars.put("shipmentId", event.shipmentId());
                businessVars.put("truckId", event.truckId());
                processNotification(businessId, "Shipment Matched", businessVars);
            }
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "trip.started", groupId = "notification-service-group")
    public void consumeTripStarted(TripStartedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming TripStartedEvent for trip: {}", event.tripId());
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("tripId", event.tripId());

            // Notify Driver
            processNotification(event.driverId(), "Trip Started", variables);

            // Notify Business Owner
            UUID businessId = fetchBusinessOwnerId(event.shipmentId());
            if (businessId != null) {
                processNotification(businessId, "Trip Started", variables);
            }
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "trip.completed", groupId = "notification-service-group")
    public void consumeTripCompleted(TripCompletedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming TripCompletedEvent for trip: {}", event.tripId());
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("tripId", event.tripId());

            // Notify Driver
            processNotification(event.driverId(), "Trip Completed", variables);

            // Notify Business Owner
            UUID businessId = fetchBusinessOwnerId(event.shipmentId());
            if (businessId != null) {
                processNotification(businessId, "Trip Completed", variables);
                // Also trigger review request to business owner
                processNotification(businessId, "Review Request", variables);
            }
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "trip.cancelled", groupId = "notification-service-group")
    public void consumeTripCancelled(String message) {
        log.info("Consuming raw trip.cancelled: {}", message);
        try {
            TripCancelledEvent event = objectMapper.readValue(message, TripCancelledEvent.class);
            setupMdc(event.correlationId(), event.eventId());
            log.info("Consuming TripCancelledEvent for trip: {}", event.tripId());

            Map<String, Object> variables = new HashMap<>();
            variables.put("tripId", event.tripId());
            variables.put("reason", event.reason() != null ? event.reason() : "N/A");

            // We can resolve driverId and businessId by querying tracking service, or send dummy alerts.
            // As a robust placeholder: send mock alerts to a simulated recipient to verify.
            UUID dummyId = UUID.randomUUID();
            processNotification(dummyId, "Trip Cancelled", variables);
        } catch (Exception e) {
            log.error("Error processing trip.cancelled message: {}", e.getMessage());
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "review-events", groupId = "notification-service-group")
    public void consumeReviewCreated(ReviewCreatedEvent event) {
        setupMdc(event.correlationId(), event.eventId());
        log.info("Consuming ReviewCreatedEvent for review: {}", event.reviewId());
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("reviewId", event.reviewId());
            variables.put("rating", event.rating());

            // Notify reviewee
            processNotification(event.revieweeId(), "Welcome Email", variables);
        } finally {
            MDC.clear();
        }
    }

    private void processNotification(UUID recipientId, String templateName, Map<String, Object> variables) {
        try {
            NotificationTemplate template = templateRepository.findByNameAndIsDeletedFalse(templateName)
                    .orElseThrow(() -> new TemplateNotFoundException("Template not found: " + templateName));

            // Populate base variables if needed
            variables.putIfAbsent("firstName", "Customer");

            String title = TemplateEngine.process(template.getTitleTemplate(), variables);
            String body = TemplateEngine.process(template.getBodyTemplate(), variables);

            notificationService.sendDirectNotification(recipientId, template.getType(), template.getChannel(), title, body);
        } catch (Exception e) {
            log.error("Failed to compile and send template notification for '{}' to: {}. Error: {}", 
                    templateName, recipientId, e.getMessage());
        }
    }

    private UUID fetchBusinessOwnerId(UUID shipmentId) {
        try {
            ShipmentFeignClient.InternalShipmentResponse shipment = shipmentFeignClient.getShipment(shipmentId);
            if (shipment != null) {
                return shipment.businessId();
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve business owner ID via Feign for shipment: {}. Error: {}", shipmentId, e.getMessage());
        }
        return null;
    }

    private void setupMdc(String correlationId, Object eventId) {
        MDC.clear();
        if (correlationId != null) MDC.put("correlationId", correlationId);
        if (eventId != null) MDC.put("traceId", eventId.toString());
    }
}
