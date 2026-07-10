package com.smartlogistics.shipmentservice.controller;

import com.smartlogistics.shipmentservice.dto.ShipmentDto;
import com.smartlogistics.shipmentservice.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shipments/{id}/status")
@RequiredArgsConstructor
@Tag(name = "Shipment Status", description = "Manage shipment status transitions")
public class ShipmentStatusController {

    private final ShipmentService shipmentService;

    @PutMapping
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Update shipment status")
    public ResponseEntity<ShipmentDto.Response> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ShipmentDto.StatusUpdateRequest request) {
        return ResponseEntity.ok(shipmentService.updateStatus(id, request));
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Get status history for a shipment")
    public ResponseEntity<List<ShipmentDto.StatusHistoryResponse>> getStatusHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(shipmentService.getStatusHistory(id));
    }

    @PutMapping("/pricing")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Update pricing for a shipment")
    public ResponseEntity<ShipmentDto.PricingResponse> updatePricing(
            @PathVariable UUID id,
            @Valid @RequestBody ShipmentDto.PricingUpdateRequest request) {
        return ResponseEntity.ok(shipmentService.updatePricing(id, request));
    }
}
