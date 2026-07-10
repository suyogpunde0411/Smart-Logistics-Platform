package com.smartlogistics.trackingservice.controller;

import com.smartlogistics.trackingservice.dto.CheckpointDto;
import com.smartlogistics.trackingservice.service.CheckpointService;
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
@RequestMapping("/api/v1/trips/{id}/checkpoints")
@RequiredArgsConstructor
@Tag(name = "Checkpoint Management", description = "Endpoints for managing route checkpoints")
public class CheckpointController {

    private final CheckpointService checkpointService;

    @PostMapping
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Manually add a planned checkpoint to trip route")
    public ResponseEntity<CheckpointDto.Response> addCheckpoint(
            @PathVariable("id") UUID tripId,
            @Valid @RequestBody CheckpointDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checkpointService.addCheckpoint(tripId, request));
    }

    @PutMapping("/{checkpointId}/reach")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Mark checkpoint as reached")
    public ResponseEntity<CheckpointDto.Response> reachCheckpoint(
            @PathVariable("id") UUID tripId,
            @PathVariable("checkpointId") UUID checkpointId) {
        return ResponseEntity.ok(checkpointService.reachCheckpoint(tripId, checkpointId));
    }

    @PutMapping("/{checkpointId}/depart")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Mark checkpoint as departed")
    public ResponseEntity<CheckpointDto.Response> departCheckpoint(
            @PathVariable("id") UUID tripId,
            @PathVariable("checkpointId") UUID checkpointId) {
        return ResponseEntity.ok(checkpointService.departCheckpoint(tripId, checkpointId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get all checkpoints for a trip")
    public ResponseEntity<List<CheckpointDto.Response>> getCheckpoints(@PathVariable("id") UUID tripId) {
        return ResponseEntity.ok(checkpointService.getCheckpoints(tripId));
    }
}
