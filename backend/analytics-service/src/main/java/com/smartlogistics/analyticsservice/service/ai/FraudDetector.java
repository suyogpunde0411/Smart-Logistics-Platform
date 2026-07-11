package com.smartlogistics.analyticsservice.service.ai;

import java.util.UUID;

public interface FraudDetector {
    /**
     * Evaluate fraud risk probability based on historical disputes and reviews.
     */
    Double calculateFraudRiskScore(UUID tripId, UUID driverId, UUID businessId);
}
