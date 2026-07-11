package com.smartlogistics.reviewservice.service;

import com.smartlogistics.reviewservice.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReviewService {
    ReviewDto.ReviewResponse createReview(UUID reviewerId, ReviewDto.CreateRequest request);
    ReviewDto.ReviewResponse updateReview(UUID userId, UUID id, ReviewDto.UpdateRequest request);
    void deleteReview(UUID userId, UUID id);
    ReviewReplyDto.ReviewReplyResponse replyToReview(UUID reviewId, ReviewReplyDto.Request request, UUID replierId);
    ReviewReportDto.ReviewReportResponse reportReview(UUID reviewId, ReviewReportDto.Request request, UUID reporterId);
    void likeReview(UUID reviewId, UUID userId);
    ReviewDisputeDto.ReviewDisputeResponse disputeReview(UUID reviewId, ReviewDisputeDto.Request request, UUID userId);
    ReviewDisputeDto.ReviewDisputeResponse resolveDispute(UUID disputeId, ReviewDisputeDto.ResolveRequest request);
    Page<ReviewDto.ReviewResponse> searchReviews(
            UUID revieweeId, UUID reviewerId, UUID tripId, String role, String status,
            Integer minRating, Integer maxRating, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Double getAverageRating(UUID userId);
    
    TrustScoreDto.TrustScoreResponse getTrustScore(UUID userId);
    List<TrustScoreDto.TrustScoreHistoryResponse> getTrustScoreHistory(UUID userId);
    
    // Internal/Event handlers
    void handleUserDeleted(UUID userId);
}
