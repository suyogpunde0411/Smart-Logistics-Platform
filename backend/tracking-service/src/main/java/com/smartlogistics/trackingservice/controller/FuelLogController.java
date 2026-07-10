package com.smartlogistics.trackingservice.controller;

import com.smartlogistics.trackingservice.dto.FuelLogDto;
import com.smartlogistics.trackingservice.service.FuelLogService;
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
@RequestMapping("/api/v1/trips/{id}/fuel")
@RequiredArgsConstructor
@Tag(name = "Fuel Logging", description = "Endpoints for fuel logging and audits")
public class FuelLogController {

    private final FuelLogService fuelLogService;

    @PostMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Log fuel refueling details")
    public ResponseEntity<FuelLogDto.Response> addFuelLog(
            @PathVariable("id") UUID tripId,
            @Valid @RequestBody FuelLogDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fuelLogService.addFuelLog(tripId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get refueling log history")
    public ResponseEntity<List<FuelLogDto.Response>> getFuelLogs(@PathVariable("id") UUID tripId) {
        return ResponseEntity.ok(fuelLogService.getFuelLogs(tripId));
    }
}
