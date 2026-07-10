package com.smartlogistics.trackingservice.controller;

import com.smartlogistics.trackingservice.dto.TripDelayDto;
import com.smartlogistics.trackingservice.service.DelayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trips/{id}/delays")
@RequiredArgsConstructor
@Tag(name = "Delay Management", description = "Endpoints for tracking delays en route")
public class DelayController {

    private final DelayService delayService;

    @PostMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Record a delay start event")
    public ResponseEntity<TripDelayDto.Response> recordDelay(
            @PathVariable("id") UUID tripId,
            @Valid @RequestBody TripDelayDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(delayService.addTripDelay(tripId, request));
    }

    @PutMapping("/{delayId}/resolve")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Mark delay event as resolved")
    public ResponseEntity<TripDelayDto.Response> resolveDelay(
            @PathVariable("id") UUID tripId,
            @PathVariable("delayId") UUID delayId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(delayService.resolveTripDelay(tripId, delayId, endTime));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get historical delays for a trip")
    public ResponseEntity<List<TripDelayDto.Response>> getDelays(@PathVariable("id") UUID tripId) {
        return ResponseEntity.ok(delayService.getTripDelays(tripId));
    }
}
