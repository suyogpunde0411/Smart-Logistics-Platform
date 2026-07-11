package com.smartlogistics.reviewservice.controller;

import com.smartlogistics.reviewservice.dto.TrustScoreDto;
import com.smartlogistics.reviewservice.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews/trust-score")
@RequiredArgsConstructor
@Tag(name = "Trust Score Management", description = "Endpoints for viewing user trust scores and reputation history")
public class TrustScoreController {

    private final ReviewService reviewService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get trust score statistics for a user")
    public ResponseEntity<TrustScoreDto.TrustScoreResponse> getTrustScore(@PathVariable UUID userId) {
        return ResponseEntity.ok(reviewService.getTrustScore(userId));
    }

    @GetMapping("/{userId}/history")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get trust score reputation history for a user")
    public ResponseEntity<List<TrustScoreDto.TrustScoreHistoryResponse>> getTrustScoreHistory(@PathVariable UUID userId) {
        return ResponseEntity.ok(reviewService.getTrustScoreHistory(userId));
    }
}
