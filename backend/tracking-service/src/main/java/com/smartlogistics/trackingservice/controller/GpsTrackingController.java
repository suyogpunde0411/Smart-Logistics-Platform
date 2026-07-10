package com.smartlogistics.trackingservice.controller;

import com.smartlogistics.trackingservice.dto.GpsLocationDto;
import com.smartlogistics.trackingservice.service.GpsTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trips/{id}/gps")
@RequiredArgsConstructor
@Tag(name = "GPS Tracking", description = "Endpoints for vehicle GPS tracking and coordinates history")
public class GpsTrackingController {

    private final GpsTrackingService gpsTrackingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Record current GPS telemetry point")
    public ResponseEntity<GpsLocationDto.Response> recordGpsLocation(
            @PathVariable("id") UUID tripId,
            @Valid @RequestBody GpsLocationDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gpsTrackingService.recordGpsLocation(tripId, request));
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get historical GPS tracking coordinates with pagination")
    public ResponseEntity<Page<GpsLocationDto.Response>> getGpsHistory(
            @PathVariable("id") UUID tripId,
            Pageable pageable) {
        return ResponseEntity.ok(gpsTrackingService.getGpsLocationHistory(tripId, pageable));
    }

    @GetMapping("/replay")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Replay coordinates route chronologically")
    public ResponseEntity<List<GpsLocationDto.Response>> getRouteReplay(@PathVariable("id") UUID tripId) {
        return ResponseEntity.ok(gpsTrackingService.getRouteReplay(tripId));
    }
}
