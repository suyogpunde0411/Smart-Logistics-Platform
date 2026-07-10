package com.smartlogistics.trackingservice.client;

import com.smartlogistics.trackingservice.config.TrackingFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "truck-service", contextId = "trackingTruckClient", configuration = TrackingFeignConfig.class)
public interface TruckClient {

    @GetMapping("/internal/trucks/{id}")
    InternalTruckResponse getTruck(@PathVariable("id") UUID id);

    record InternalTruckResponse(
            UUID id,
            UUID ownerId,
            String licensePlate,
            String type,
            Double capacity,
            String status
    ) {}
}
