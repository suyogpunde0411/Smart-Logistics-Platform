package com.smartlogistics.shipmentservice.controller;

import com.smartlogistics.shared.enums.ShipmentStatus;
import com.smartlogistics.shipmentservice.dto.ShipmentDto;
import com.smartlogistics.shipmentservice.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
@Tag(name = "Shipment", description = "Shipment management APIs")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Create a new shipment")
    public ResponseEntity<ShipmentDto.Response> createShipment(
            @Valid @RequestBody ShipmentDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.createShipment(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Get shipment by ID")
    public ResponseEntity<ShipmentDto.Response> getShipment(@PathVariable UUID id) {
        return ResponseEntity.ok(shipmentService.getShipment(id));
    }

    @GetMapping("/tracking/{trackingNumber}")
    @Operation(summary = "Track shipment by tracking number (public)")
    public ResponseEntity<ShipmentDto.TrackingResponse> trackShipment(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(shipmentService.trackShipment(trackingNumber));
    }

    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "List active shipment categories")
    public ResponseEntity<List<ShipmentDto.CategoryResponse>> getCategories() {
        return ResponseEntity.ok(shipmentService.getActiveCategories());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Update shipment details")
    public ResponseEntity<ShipmentDto.Response> updateShipment(
            @PathVariable UUID id,
            @Valid @RequestBody ShipmentDto.UpdateRequest request) {
        return ResponseEntity.ok(shipmentService.updateShipment(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Cancel and soft-delete shipment")
    public ResponseEntity<Void> deleteShipment(@PathVariable UUID id) {
        shipmentService.deleteShipment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Cancel a shipment")
    public ResponseEntity<ShipmentDto.Response> cancelShipment(
            @PathVariable UUID id,
            @RequestParam(required = false) String remarks) {
        return ResponseEntity.ok(shipmentService.cancelShipment(id, remarks));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Search shipments with filters")
    public ResponseEntity<Page<ShipmentDto.Response>> searchShipments(
            @RequestParam(required = false) UUID businessOwnerId,
            @RequestParam(required = false) ShipmentStatus status,
            @RequestParam(required = false) String cargoType,
            @RequestParam(required = false) Double minWeight,
            @RequestParam(required = false) Double maxWeight,
            @RequestParam(required = false) String requiredTruckType,
            @RequestParam(required = false) String originCity,
            @RequestParam(required = false) String destinationCity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime pickupAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deliveryBefore,
            Pageable pageable) {
        return ResponseEntity.ok(shipmentService.searchShipments(
                businessOwnerId, status, cargoType, minWeight, maxWeight,
                requiredTruckType, originCity, destinationCity, pickupAfter, deliveryBefore, pageable));
    }

    @GetMapping("/nearby")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Find shipments near origin location (radius search)")
    public ResponseEntity<Page<ShipmentDto.Response>> findNearby(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radius,
            Pageable pageable) {
        return ResponseEntity.ok(shipmentService.findShipmentsNearOrigin(latitude, longitude, radius, pageable));
    }

    @PostMapping("/{id}/items")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Add an item to a shipment")
    public ResponseEntity<ShipmentDto.ItemResponse> addItem(
            @PathVariable UUID id,
            @Valid @RequestBody ShipmentDto.ItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.addItem(id, request));
    }

    @GetMapping("/{id}/items")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "List shipment items")
    public ResponseEntity<List<ShipmentDto.ItemResponse>> getItems(@PathVariable UUID id) {
        return ResponseEntity.ok(shipmentService.getItems(id));
    }

    @GetMapping("/{id}/pricing")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Get shipment pricing")
    public ResponseEntity<ShipmentDto.PricingResponse> getPricing(@PathVariable UUID id) {
        return ResponseEntity.ok(shipmentService.getPricing(id));
    }

    @PutMapping("/{id}/pricing")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Update pricing for a shipment")
    public ResponseEntity<ShipmentDto.PricingResponse> updatePricing(
            @PathVariable UUID id,
            @Valid @RequestBody ShipmentDto.PricingUpdateRequest request) {
        return ResponseEntity.ok(shipmentService.updatePricing(id, request));
    }

    // Pickup & Drop scheduling

    @PostMapping("/{id}/pickup")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Schedule pickup for a shipment")
    public ResponseEntity<ShipmentDto.PickupResponse> schedulePickup(
            @PathVariable UUID id,
            @Valid @RequestBody ShipmentDto.PickupScheduleRequest request) {
        return ResponseEntity.ok(shipmentService.schedulePickup(id, request));
    }

    @PostMapping("/{id}/drop")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Schedule drop for a shipment")
    public ResponseEntity<ShipmentDto.DropResponse> scheduleDrop(
            @PathVariable UUID id,
            @Valid @RequestBody ShipmentDto.DropScheduleRequest request) {
        return ResponseEntity.ok(shipmentService.scheduleDrop(id, request));
    }
}
