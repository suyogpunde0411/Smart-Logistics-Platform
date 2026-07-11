package com.smartlogistics.reviewservice.controller;

import com.smartlogistics.reviewservice.dto.*;
import com.smartlogistics.reviewservice.service.ReviewService;
import com.smartlogistics.shared.security.CurrentUserUtil;
import com.smartlogistics.shared.security.RoleUtil;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Management", description = "Endpoints for managing reviews and ratings")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER')")
    @Operation(summary = "Submit a new review for a completed trip")
    public ResponseEntity<ReviewDto.ReviewResponse> createReview(@Valid @RequestBody ReviewDto.CreateRequest request) {
        UUID reviewerId = CurrentUserUtil.getUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(reviewerId, request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Update review (editable for 24h by owner)")
    public ResponseEntity<ReviewDto.ReviewResponse> updateReview(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewDto.UpdateRequest request) {
        UUID userId = CurrentUserUtil.getUserId();
        return ResponseEntity.ok(reviewService.updateReview(userId, id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Delete review (within 24h by owner or anytime by admin)")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        UUID userId = CurrentUserUtil.getUserId();
        reviewService.deleteReview(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reply")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Reply to a review (by the reviewee)")
    public ResponseEntity<ReviewReplyDto.ReviewReplyResponse> replyToReview(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewReplyDto.Request request) {
        UUID replierId = CurrentUserUtil.getUserId();
        return ResponseEntity.ok(reviewService.replyToReview(id, request, replierId));
    }

    @PostMapping("/{id}/report")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER')")
    @Operation(summary = "Report a review for abuse or spam")
    public ResponseEntity<ReviewReportDto.ReviewReportResponse> reportReview(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewReportDto.Request request) {
        UUID reporterId = CurrentUserUtil.getUserId();
        return ResponseEntity.ok(reviewService.reportReview(id, request, reporterId));
    }

    @PostMapping("/{id}/like")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER')")
    @Operation(summary = "Toggle like/unlike for a review")
    public ResponseEntity<Void> likeReview(@PathVariable UUID id) {
        UUID userId = CurrentUserUtil.getUserId();
        reviewService.likeReview(id, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/dispute")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER')")
    @Operation(summary = "File a dispute against a review by the reviewee")
    public ResponseEntity<ReviewDisputeDto.ReviewDisputeResponse> disputeReview(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewDisputeDto.Request request) {
        UUID userId = CurrentUserUtil.getUserId();
        return ResponseEntity.ok(reviewService.disputeReview(id, request, userId));
    }

    @PutMapping("/disputes/{disputeId}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Resolve review dispute (Admin only)")
    public ResponseEntity<ReviewDisputeDto.ReviewDisputeResponse> resolveDispute(
            @PathVariable UUID disputeId,
            @Valid @RequestBody ReviewDisputeDto.ResolveRequest request) {
        return ResponseEntity.ok(reviewService.resolveDispute(disputeId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Search, filter, and paginate reviews history")
    public ResponseEntity<Page<ReviewDto.ReviewResponse>> searchReviews(
            @RequestParam(required = false) UUID revieweeId,
            @RequestParam(required = false) UUID reviewerId,
            @RequestParam(required = false) UUID tripId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(reviewService.searchReviews(
                revieweeId, reviewerId, tripId, role, status, minRating, maxRating, startDate, endDate, pageable));
    }

    private List<String> getRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
