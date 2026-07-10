package com.smartlogistics.trackingservice.controller;

import com.smartlogistics.trackingservice.dto.*;
import com.smartlogistics.trackingservice.service.TripService;
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
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
@Tag(name = "Trip Management", description = "Endpoints for managing logistics trip lifecycles")
public class TripController {

    private final TripService tripService;

    @PostMapping
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Manually create a new trip")
    public ResponseEntity<TripDto.Response> createTrip(@Valid @RequestBody TripDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTripManual(request));
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Assign a driver and truck to a trip")
    public ResponseEntity<TripDto.Response> assignDriverAndTruck(
            @PathVariable UUID id,
            @Valid @RequestBody TripDto.AssignRequest request) {
        return ResponseEntity.ok(tripService.assignDriverAndTruck(id, request));
    }

    @PutMapping("/{id}/ready")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Mark trip as ready for departure")
    public ResponseEntity<TripDto.Response> setTripReady(@PathVariable UUID id) {
        return ResponseEntity.ok(tripService.setTripReady(id));
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Start the trip")
    public ResponseEntity<TripDto.Response> startTrip(@PathVariable UUID id) {
        return ResponseEntity.ok(tripService.startTrip(id));
    }

    @PostMapping("/{id}/pause")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Pause the trip")
    public ResponseEntity<TripDto.Response> pauseTrip(@PathVariable UUID id) {
        return ResponseEntity.ok(tripService.pauseTrip(id));
    }

    @PostMapping("/{id}/resume")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Resume the trip")
    public ResponseEntity<TripDto.Response> resumeTrip(@PathVariable UUID id) {
        return ResponseEntity.ok(tripService.resumeTrip(id));
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Complete the trip")
    public ResponseEntity<TripDto.Response> completeTrip(@PathVariable UUID id) {
        return ResponseEntity.ok(tripService.completeTrip(id));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Cancel the trip")
    public ResponseEntity<TripDto.Response> cancelTrip(
            @PathVariable UUID id,
            @RequestParam String reason) {
        return ResponseEntity.ok(tripService.cancelTrip(id, reason));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get trip by ID")
    public ResponseEntity<TripDto.Response> getTripById(@PathVariable UUID id) {
        return ResponseEntity.ok(tripService.getTripById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Search and filter trips")
    public ResponseEntity<Page<TripDto.Response>> searchTrips(
            @RequestParam(required = false) UUID tripId,
            @RequestParam(required = false) UUID shipmentId,
            @RequestParam(required = false) UUID truckId,
            @RequestParam(required = false) UUID driverId,
            @RequestParam(required = false) UUID businessId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(tripService.searchTrips(tripId, shipmentId, truckId, driverId, businessId, status, startDate, endDate, pageable));
    }

    @GetMapping("/{id}/timeline")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get historical trip audit timelines")
    public ResponseEntity<List<TripTimelineDto.Response>> getTripTimeline(@PathVariable UUID id) {
        return ResponseEntity.ok(tripService.getTripTimeline(id));
    }

    @GetMapping("/{id}/summary")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get completed trip summary metrics")
    public ResponseEntity<TripSummaryDto.Response> getTripSummary(@PathVariable UUID id) {
        return ResponseEntity.ok(tripService.getTripSummary(id));
    }
}
