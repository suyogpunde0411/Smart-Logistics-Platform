package com.smartlogistics.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "analytics-service", path = "/internal/analytics")
public interface AnalyticsFeignClient {

    @PostMapping("/trips")
    void recordTripMetrics(@RequestBody InternalTripMetricsRequest request);

    record InternalTripMetricsRequest(
            UUID tripId,
            UUID driverId,
            Double distance,
            Double durationMinutes,
            Double revenue
    ) {}
}
