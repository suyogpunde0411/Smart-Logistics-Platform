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
@RequestMapping("/api/v1/shipments/{shipmentId}/documents")
@RequiredArgsConstructor
@Tag(name = "Shipment Documents", description = "Manage shipment document metadata")
public class ShipmentDocumentController {

    private final ShipmentService shipmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Upload document metadata for a shipment")
    public ResponseEntity<ShipmentDto.DocumentResponse> uploadDocument(
            @PathVariable UUID shipmentId,
            @Valid @RequestBody ShipmentDto.DocumentUploadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.uploadDocument(shipmentId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Get all documents for a shipment")
    public ResponseEntity<List<ShipmentDto.DocumentResponse>> getDocuments(@PathVariable UUID shipmentId) {
        return ResponseEntity.ok(shipmentService.getDocuments(shipmentId));
    }

    @DeleteMapping("/{documentId}")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Delete a shipment document")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable UUID shipmentId,
            @PathVariable UUID documentId) {
        shipmentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}
