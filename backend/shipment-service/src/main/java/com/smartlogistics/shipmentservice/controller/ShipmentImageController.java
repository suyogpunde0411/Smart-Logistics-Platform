package com.smartlogistics.shipmentservice.controller;

import com.smartlogistics.shipmentservice.dto.ShipmentDto;
import com.smartlogistics.shipmentservice.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shipments/{shipmentId}/images")
@RequiredArgsConstructor
@Tag(name = "Shipment Images", description = "Manage shipment images")
public class ShipmentImageController {

    private final ShipmentService shipmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Upload image for a shipment")
    public ResponseEntity<ShipmentDto.ImageResponse> uploadImage(
            @PathVariable UUID shipmentId,
            @Valid @RequestBody ShipmentDto.ImageUploadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.uploadImage(shipmentId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Get all images for a shipment")
    public ResponseEntity<List<ShipmentDto.ImageResponse>> getImages(@PathVariable UUID shipmentId) {
        return ResponseEntity.ok(shipmentService.getImages(shipmentId));
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Delete a shipment image")
    public ResponseEntity<Void> deleteImage(
            @PathVariable UUID shipmentId,
            @PathVariable UUID imageId) {
        shipmentService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}
