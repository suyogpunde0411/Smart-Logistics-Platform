package com.smartlogistics.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "truck-service", path = "/internal/trucks")
public interface TruckFeignClient {

    @GetMapping("/{id}")
    InternalTruckResponse getTruck(@PathVariable("id") UUID id);

    record InternalTruckResponse(
            UUID id,
            UUID ownerId,
            String licensePlate,
            String type,
            Double capacity,
            String status // AVAILABLE, ON_TRIP, MAINTENANCE
    ) {}
}
