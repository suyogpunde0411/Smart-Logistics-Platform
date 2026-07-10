package com.smartlogistics.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "shipment-service", path = "/internal/shipments")
public interface ShipmentFeignClient {

    @GetMapping("/{id}")
    InternalShipmentResponse getShipment(@PathVariable("id") UUID id);

    record InternalShipmentResponse(
            UUID id,
            UUID businessId,
            String origin,
            String destination,
            Double weight,
            Double budget,
            String status // CREATED, MATCHED, IN_TRANSIT, DELIVERED
    ) {}
}
