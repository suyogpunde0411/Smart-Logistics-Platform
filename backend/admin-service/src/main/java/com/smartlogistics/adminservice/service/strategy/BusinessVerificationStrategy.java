package com.smartlogistics.adminservice.service.strategy;

import com.smartlogistics.adminservice.entity.AdminAction;
import com.smartlogistics.adminservice.entity.VerificationRequest;
import com.smartlogistics.adminservice.events.AdminKafkaProducer;
import com.smartlogistics.adminservice.events.BusinessVerifiedEvent;
import com.smartlogistics.adminservice.repository.AdminActionRepository;
import com.smartlogistics.adminservice.repository.VerificationRequestRepository;
import com.smartlogistics.adminservice.client.UserVerifyClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BusinessVerificationStrategy implements VerificationStrategy {

    private final VerificationRequestRepository requestRepository;
    private final AdminActionRepository adminActionRepository;
    private final AdminKafkaProducer kafkaProducer;
    private final UserVerifyClient userVerifyClient;

    @Override
    public String getVerificationType() {
        return "BUSINESS";
    }

    @Override
    public void process(VerificationRequest request, String decision, String comment, UUID adminId) {
        log.info("Processing Business Verification for request: {}, decision: {}", request.getId(), decision);

        request.setStatus(decision);
        request.setComments(comment);
        requestRepository.save(request);

        adminActionRepository.save(AdminAction.builder()
                .adminId(adminId)
                .actionType("VERIFY_BUSINESS")
                .targetEntityId(request.getEntityId())
                .targetEntityType("BUSINESS")
                .details("Business profile verification decision: " + decision + ". Comment: " + comment)
                .build());

        // Sync via Feign
        try {
            userVerifyClient.verifyBusinessProfile(request.getEntityId(), decision);
        } catch (Exception e) {
            log.error("Failed to sync business status with user-service via Feign: {}", e.getMessage());
        }

        // Publish to Kafka
        kafkaProducer.publishBusinessVerified(new BusinessVerifiedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                null,
                request.getEntityId(),
                decision,
                comment
        ));
    }
}
