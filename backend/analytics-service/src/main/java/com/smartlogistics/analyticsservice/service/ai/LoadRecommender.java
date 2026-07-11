package com.smartlogistics.analyticsservice.service.ai;

import java.util.List;
import java.util.UUID;

public interface LoadRecommender {
    /**
     * Recommend load shipment IDs for a given truck to reduce empty miles.
     */
    List<UUID> recommendLoads(UUID truckId, int limit);
}
