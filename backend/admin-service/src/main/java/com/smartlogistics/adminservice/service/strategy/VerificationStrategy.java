package com.smartlogistics.adminservice.service.strategy;

import com.smartlogistics.adminservice.entity.VerificationRequest;
import java.util.UUID;

public interface VerificationStrategy {
    String getVerificationType(); // DRIVER, BUSINESS, FLEET, TRUCK
    void process(VerificationRequest request, String decision, String comment, UUID adminId);
}
