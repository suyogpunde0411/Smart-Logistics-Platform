package com.smartlogistics.adminservice.service.strategy;

import com.smartlogistics.adminservice.entity.AdminAction;
import com.smartlogistics.adminservice.entity.VerificationRequest;
import com.smartlogistics.adminservice.events.AdminKafkaProducer;
import com.smartlogistics.adminservice.events.TruckVerifiedEvent;
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
public class TruckVerificationStrategy implements VerificationStrategy {

    private final VerificationRequestRepository requestRepository;
    private final AdminActionRepository adminActionRepository;
    private final AdminKafkaProducer kafkaProducer;

    @Override
    public String getVerificationType() {
        return "TRUCK";
    }

    @Override
    public void process(VerificationRequest request, String decision, String comment, UUID adminId) {
        log.info("Processing Truck Verification for request: {}, decision: {}", request.getId(), decision);

        request.setStatus(decision);
        request.setComments(comment);
        requestRepository.save(request);

        adminActionRepository.save(AdminAction.builder()
                .adminId(adminId)
                .actionType("VERIFY_TRUCK")
                .targetEntityId(request.getEntityId())
                .targetEntityType("TRUCK")
                .details("Truck verification decision: " + decision + ". Comment: " + comment)
                .build());

        // Publish to Kafka
        kafkaProducer.publishTruckVerified(new TruckVerifiedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                null,
                request.getEntityId(),
                decision,
                comment
        ));
    }
}
