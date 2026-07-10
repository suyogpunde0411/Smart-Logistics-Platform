package com.smartlogistics.truckservice.controller;

import com.smartlogistics.shared.dto.TruckDTO;
import com.smartlogistics.truckservice.service.TruckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trucks")
@RequiredArgsConstructor
public class TruckController {

    private final TruckService truckService;

    @PostMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<TruckDTO.Response> registerTruck(@Valid @RequestBody TruckDTO.RegisterRequest request) {
        TruckDTO.Response response = truckService.registerTruck(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<TruckDTO.Response> getTruck(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(truckService.getTruck(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<TruckDTO.Response> updateTruck(@PathVariable("id") UUID id, @Valid @RequestBody TruckDTO.UpdateRequest request) {
        return ResponseEntity.ok(truckService.updateTruck(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<Void> deleteTruck(@PathVariable("id") UUID id) {
        truckService.deleteTruck(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<Page<TruckDTO.Response>> searchTrucks(
            @RequestParam(value = "regNum", required = false) String regNum,
            @RequestParam(value = "ownerId", required = false) UUID ownerId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "availStatus", required = false) String availStatus,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "minWeight", required = false) Double minWeight,
            @RequestParam(value = "minVolume", required = false) Double minVolume,
            Pageable pageable) {
        Page<TruckDTO.Response> results = truckService.searchTrucks(
                regNum, ownerId, status, availStatus, active, minWeight, minVolume, pageable);
        return ResponseEntity.ok(results);
    }

    @PutMapping("/{id}/availability")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<TruckDTO.AvailabilityDto> toggleAvailability(
            @PathVariable("id") UUID id, @Valid @RequestBody TruckDTO.AvailabilityToggle request) {
        return ResponseEntity.ok(truckService.toggleAvailability(id, request));
    }

    @PutMapping("/{id}/location")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<TruckDTO.LocationDto> updateLocation(
            @PathVariable("id") UUID id, @Valid @RequestBody TruckDTO.LocationDto request) {
        return ResponseEntity.ok(truckService.updateLocation(id, request));
    }

    @GetMapping("/nearby")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<Page<TruckDTO.Response>> findNearbyTrucks(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("radius") Double radius,
            Pageable pageable) {
        Page<TruckDTO.Response> results = truckService.findNearbyTrucks(latitude, longitude, radius, pageable);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/{id}/documents")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<TruckDTO.DocumentDto> uploadDocument(
            @PathVariable("id") UUID id, @Valid @RequestBody TruckDTO.DocumentUploadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(truckService.uploadDocument(id, request));
    }

    @DeleteMapping("/documents/{docId}")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<Void> deleteDocument(@PathVariable("docId") UUID docId) {
        truckService.deleteDocument(docId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/insurance")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<TruckDTO.InsuranceDto> addInsurance(
            @PathVariable("id") UUID id, @Valid @RequestBody TruckDTO.InsuranceCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(truckService.addInsurance(id, request));
    }

    @PostMapping("/{id}/maintenance")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_OWNER', 'ADMIN')")
    public ResponseEntity<TruckDTO.MaintenanceDto> addMaintenanceLog(
            @PathVariable("id") UUID id, @Valid @RequestBody TruckDTO.MaintenanceCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(truckService.addMaintenanceLog(id, request));
    }
}
