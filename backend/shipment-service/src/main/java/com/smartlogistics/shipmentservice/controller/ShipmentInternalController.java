package com.smartlogistics.shipmentservice.controller;

import com.smartlogistics.common.client.ShipmentFeignClient.InternalShipmentResponse;
import com.smartlogistics.shipmentservice.entity.Shipment;
import com.smartlogistics.shipmentservice.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/internal/shipments")
@RequiredArgsConstructor
public class ShipmentInternalController {

    private final ShipmentService shipmentService;

    @GetMapping("/{id}")
    public ResponseEntity<InternalShipmentResponse> getShipment(@PathVariable UUID id) {
        Shipment shipment = shipmentService.findShipmentById(id);

        InternalShipmentResponse response = new InternalShipmentResponse(
                shipment.getId(),
                shipment.getBusinessOwnerId(),
                shipment.getOriginAddress(),
                shipment.getDestinationAddress(),
                shipment.getTotalWeight(),
                shipment.getBudgetAmount(),
                shipment.getStatus().name()
        );

        return ResponseEntity.ok(response);
    }
}
