package com.smartlogistics.analyticsservice.service.ai;

import java.util.Map;

public interface DemandForecaster {
    /**
     * Forecast demand volume (number of shipments) for a given city and state.
     * Prepare historical datasets to be consumed by deep learning models.
     */
    Map<String, Double> forecastDemand(String city, String state, int daysAhead);
}
