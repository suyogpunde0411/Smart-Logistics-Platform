package com.smartlogistics.analyticsservice.service.ai;

import java.util.UUID;

public interface PricePredictor {
    /**
     * Predict dynamic shipment price using current matching weight, type, and route indices.
     */
    Double predictPrice(UUID shipmentId, String route, Double weightKg, String cargoType);
}
