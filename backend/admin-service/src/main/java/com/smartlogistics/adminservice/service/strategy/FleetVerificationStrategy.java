package com.smartlogistics.adminservice.service.strategy;

import com.smartlogistics.adminservice.entity.AdminAction;
import com.smartlogistics.adminservice.entity.VerificationRequest;
import com.smartlogistics.adminservice.events.AdminKafkaProducer;
import com.smartlogistics.adminservice.events.UserVerifiedEvent;
import com.smartlogistics.adminservice.repository.AdminActionRepository;
import com.smartlogistics.adminservice.repository.VerificationRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FleetVerificationStrategy implements VerificationStrategy {

    private final VerificationRequestRepository requestRepository;
    private final AdminActionRepository adminActionRepository;
    private final AdminKafkaProducer kafkaProducer;

    @Override
    public String getVerificationType() {
        return "FLEET";
    }

    @Override
    public void process(VerificationRequest request, String decision, String comment, UUID adminId) {
        log.info("Processing Fleet Verification for request: {}, decision: {}", request.getId(), decision);

        request.setStatus(decision);
        request.setComments(comment);
        requestRepository.save(request);

        adminActionRepository.save(AdminAction.builder()
                .adminId(adminId)
                .actionType("VERIFY_FLEET_OWNER")
                .targetEntityId(request.getEntityId())
                .targetEntityType("FLEET_OWNER")
                .details("Fleet owner verification decision: " + decision + ". Comment: " + comment)
                .build());

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
