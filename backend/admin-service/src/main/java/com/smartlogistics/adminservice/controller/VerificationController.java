package com.smartlogistics.adminservice.controller;

import com.smartlogistics.adminservice.dto.VerificationRequestDto;
import com.smartlogistics.adminservice.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/verifications")
@Tag(name = "Verification", description = "Verification APIs")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping
    @Operation(summary = "Submit Verification Request")
    public ResponseEntity<VerificationRequestDto> submitRequest(@RequestBody VerificationRequestDto dto) {
        return ResponseEntity.ok(verificationService.submitRequest(dto));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get All Pending Requests")
    public ResponseEntity<List<VerificationRequestDto>> getPendingRequests() {
        return ResponseEntity.ok(verificationService.getAllPendingRequests());
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve Verification Request")
    public ResponseEntity<VerificationRequestDto> approveRequest(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID adminId,
            @RequestParam String comments) {
        return ResponseEntity.ok(verificationService.approveRequest(id, adminId, comments));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject Verification Request")
    public ResponseEntity<VerificationRequestDto> rejectRequest(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID adminId,
            @RequestParam String comments) {
        return ResponseEntity.ok(verificationService.rejectRequest(id, adminId, comments));
    }
}
