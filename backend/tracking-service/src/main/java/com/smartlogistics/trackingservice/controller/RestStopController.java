package com.smartlogistics.trackingservice.controller;

import com.smartlogistics.trackingservice.dto.RestStopDto;
import com.smartlogistics.trackingservice.service.RestStopService;
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
@RequestMapping("/api/v1/trips/{id}/rest-stops")
@RequiredArgsConstructor
@Tag(name = "Rest Stop Logging", description = "Endpoints for managing driver rest stops and breaks")
public class RestStopController {

    private final RestStopService restStopService;

    @PostMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Log beginning of rest break")
    public ResponseEntity<RestStopDto.Response> recordRestStop(
            @PathVariable("id") UUID tripId,
            @Valid @RequestBody RestStopDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restStopService.startRestStop(tripId, request));
    }

    @PutMapping("/{restStopId}/end")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Log end of rest break")
    public ResponseEntity<RestStopDto.Response> endRestStop(
            @PathVariable("id") UUID tripId,
            @PathVariable("restStopId") UUID restStopId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(restStopService.endRestStop(tripId, restStopId, endTime));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get historical rest breaks for a trip")
    public ResponseEntity<List<RestStopDto.Response>> getRestStops(@PathVariable("id") UUID tripId) {
        return ResponseEntity.ok(restStopService.getRestStops(tripId));
    }
}
