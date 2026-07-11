package com.smartlogistics.adminservice.service.strategy;

import com.smartlogistics.adminservice.entity.AdminAction;
import com.smartlogistics.adminservice.entity.VerificationRequest;
import com.smartlogistics.adminservice.events.AdminKafkaProducer;
import com.smartlogistics.adminservice.events.UserVerifiedEvent;
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
public class DriverVerificationStrategy implements VerificationStrategy {

    private final VerificationRequestRepository requestRepository;
    private final AdminActionRepository adminActionRepository;
    private final AdminKafkaProducer kafkaProducer;
    private final UserVerifyClient userVerifyClient;

    @Override
    public String getVerificationType() {
        return "DRIVER";
    }

    @Override
    public void process(VerificationRequest request, String decision, String comment, UUID adminId) {
        log.info("Processing Driver Verification for request: {}, decision: {}", request.getId(), decision);

        request.setStatus(decision);
        request.setComments(comment);
        requestRepository.save(request);

        adminActionRepository.save(AdminAction.builder()
                .adminId(adminId)
                .actionType("VERIFY_DRIVER")
                .targetEntityId(request.getEntityId())
                .targetEntityType("DRIVER")
                .details("Driver profile verification decision: " + decision + ". Comment: " + comment)
                .build());

        // Synchronize with user-service
        try {
            userVerifyClient.verifyDriverProfile(request.getEntityId(), decision);
        } catch (Exception e) {
            log.error("Failed to sync driver status with user-service via Feign: {}", e.getMessage());
        }

        // Publish to Kafka
        kafkaProducer.publishUserVerified(new UserVerifiedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                null,
                request.getEntityId(),
                decision,
                comment
        ));
    }
}
