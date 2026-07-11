package com.smartlogistics.reviewservice.service;

import com.smartlogistics.common.client.UserFeignClient;
import com.smartlogistics.reviewservice.client.TrackingFeignClient;
import com.smartlogistics.reviewservice.dto.*;
import com.smartlogistics.reviewservice.entity.*;
import com.smartlogistics.reviewservice.events.ReviewKafkaProducer;
import com.smartlogistics.reviewservice.exception.*;
import com.smartlogistics.reviewservice.mapper.ReviewMapper;
import com.smartlogistics.reviewservice.repository.*;
import com.smartlogistics.shared.security.RoleUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;
    private final ReviewReplyRepository replyRepository;
    private final ReviewReportRepository reportRepository;
    private final ReviewDisputeRepository disputeRepository;
    private final ReviewLikeRepository likeRepository;
    private final TrustScoreRepository trustScoreRepository;
    private final RatingStatisticsRepository statisticsRepository;
    
    private final TrackingFeignClient trackingFeignClient;
    private final UserFeignClient userFeignClient;
    private final ReviewMapper reviewMapper;
    
    @Lazy
    private final TrustScoreService trustScoreService;
    
    private final ReviewKafkaProducer kafkaProducer;

    @Override
    @Transactional
    public ReviewDto.ReviewResponse createReview(UUID reviewerId, ReviewDto.CreateRequest request) {
        log.info("Creating review for trip: {}, reviewer: {}", request.tripId(), reviewerId);
        
        // 0. Verify reviewee profile is active
        try {
            UserFeignClient.InternalUserResponse user = userFeignClient.getUser(request.revieweeId());
            if (user == null || !"ACTIVE".equalsIgnoreCase(user.status())) {
                throw new UnauthorizedReviewException("Reviewee is not an active user on the platform");
            }
        } catch (UnauthorizedReviewException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to verify user profile for reviewee: {}", request.revieweeId(), e);
            throw new UnauthorizedReviewException("Reviewee profile verification failed: " + e.getMessage());
        }

        // 1. Fetch Trip details from tracking-service
        TrackingFeignClient.InternalTripResponse trip;
        try {
            trip = trackingFeignClient.getTrip(request.tripId());
        } catch (Exception e) {
            log.error("Failed to fetch trip details for id: {}", request.tripId(), e);
            throw new ReviewNotFoundException("Trip not found with ID: " + request.tripId());
        }
        
        // 2. Validate Trip is completed
        if (!"COMPLETED".equalsIgnoreCase(trip.status())) {
            throw new TripNotCompletedException("Cannot review a trip that is not completed. Current status: " + trip.status());
        }
        
        // 3. Reconcile reviewer/reviewee match userIds to trip driver/business owner details
        String role = request.revieweeRole().toUpperCase();
        if ("DRIVER".equals(role)) {
            if (!trip.driverId().equals(request.revieweeId())) {
                throw new UnauthorizedReviewException("Reviewee ID does not match the driver of this trip");
            }
            if (!trip.businessId().equals(reviewerId)) {
                throw new UnauthorizedReviewException("Only the business owner who booked the trip can review the driver");
            }
        } else if ("BUSINESS_OWNER".equals(role)) {
            if (!trip.businessId().equals(request.revieweeId())) {
                throw new UnauthorizedReviewException("Reviewee ID does not match the business owner of this trip");
            }
            if (!trip.driverId().equals(reviewerId)) {
                throw new UnauthorizedReviewException("Only the assigned driver can review the business owner");
            }
        } else {
            // Fleet Owner or other role validation
            if (!trip.driverId().equals(reviewerId) && !trip.businessId().equals(reviewerId)) {
                throw new UnauthorizedReviewException("Reviewer must be associated with the trip");
            }
        }
        
        // 4. Limit to one review per trip/role (prevent duplicate reviews)
        if (reviewRepository.existsByTripIdAndRevieweeRoleAndIsDeletedFalse(request.tripId(), role)) {
            throw new DuplicateReviewException("A review has already been submitted for this trip and role: " + role);
        }
        
        // 5. Build and save Review entity
        Rating rating = reviewMapper.toEntity(request.rating());
        Review review = Review.builder()
                .tripId(request.tripId())
                .reviewerId(reviewerId)
                .revieweeId(request.revieweeId())
                .revieweeRole(role)
                .comment(request.comment())
                .status("APPROVED") // Default status
                .rating(rating)
                .build();
                
        Review savedReview = reviewRepository.save(review);
        
        // 6. Recalculate RatingStatistics and TrustScore
        updateRatingStatisticsAndTrustScore(request.revieweeId(), "New review submitted");
        
        // 7. Publish Event
        kafkaProducer.publishReviewCreated(
                savedReview.getId(), 
                savedReview.getTripId(), 
                savedReview.getReviewerId(), 
                savedReview.getRevieweeId(), 
                savedReview.getRating().getOverallExperience(), 
                savedReview.getComment()
        );
        
        return reviewMapper.toDto(savedReview);
    }

    @Override
    @Transactional
    public ReviewDto.ReviewResponse updateReview(UUID userId, UUID id, ReviewDto.UpdateRequest request) {
        log.info("Updating review with ID: {}, by user: {}", id, userId);
        Review review = reviewRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + id));
                
        // Validate user is the owner of the review or admin
        boolean isAdmin = RoleUtil.hasRole("ADMIN");
        if (!review.getReviewerId().equals(userId) && !isAdmin) {
            throw new UnauthorizedReviewException("You are not authorized to update this review");
        }
        
        // Immutable after 24 hours check (unless admin)
        if (!isAdmin && review.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new UnauthorizedReviewException("Reviews cannot be updated after 24 hours of submission");
        }
        
        if (request.comment() != null) {
            review.setComment(request.comment());
        }
        
        if (request.rating() != null) {
            Rating oldRating = review.getRating();
            Rating newRating = reviewMapper.toEntity(request.rating());
            newRating.setId(oldRating.getId());
            ratingRepository.save(newRating);
            review.setRating(newRating);
        }
        
        Review updatedReview = reviewRepository.save(review);
        
        // Recalculate stats
        updateRatingStatisticsAndTrustScore(review.getRevieweeId(), "Review updated");
        
        // Publish Event
        kafkaProducer.publishReviewUpdated(
                updatedReview.getId(),
                updatedReview.getRevieweeId(),
                updatedReview.getRating().getOverallExperience(),
                updatedReview.getComment()
        );
        
        return reviewMapper.toDto(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(UUID userId, UUID id) {
        log.info("Soft-deleting review with ID: {}, by user: {}", id, userId);
        Review review = reviewRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + id));
                
        boolean isAdmin = RoleUtil.hasRole("ADMIN");
        if (!review.getReviewerId().equals(userId) && !isAdmin) {
            throw new UnauthorizedReviewException("You are not authorized to delete this review");
        }
        
        // Immutable after 24 hours check (unless admin)
        if (!isAdmin && review.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new UnauthorizedReviewException("Reviews cannot be deleted after 24 hours of submission");
        }
        
        review.setDeleted(true);
        reviewRepository.save(review);
        
        // Recalculate stats
        updateRatingStatisticsAndTrustScore(review.getRevieweeId(), "Review deleted");
    }

    @Override
    @Transactional
    public ReviewReplyDto.ReviewReplyResponse replyToReview(UUID reviewId, ReviewReplyDto.Request request, UUID replierId) {
        log.info("Replying to review: {}, replier: {}", reviewId, replierId);
        Review review = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));
                
        // Only the reviewee can reply (or admin)
        if (!review.getRevieweeId().equals(replierId)) {
            throw new UnauthorizedReviewException("Only the recipient of the review can reply to it");
        }
        
        ReviewReply reply = ReviewReply.builder()
                .review(review)
                .replierId(replierId)
                .message(request.message())
                .build();
                
        ReviewReply savedReply = replyRepository.save(reply);
        review.getReplies().add(savedReply);
        reviewRepository.save(review);
        
        return reviewMapper.toDto(savedReply);
    }

    @Override
    @Transactional
    public ReviewReportDto.ReviewReportResponse reportReview(UUID reviewId, ReviewReportDto.Request request, UUID reporterId) {
        log.info("Reporting review: {}, reporter: {}", reviewId, reporterId);
        Review review = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));
                
        // Cannot report own review
        if (review.getReviewerId().equals(reporterId)) {
            throw new UnauthorizedReviewException("Cannot report your own review");
        }
        
        ReviewReport report = ReviewReport.builder()
                .review(review)
                .reporterId(reporterId)
                .reason(request.reason())
                .status("PENDING")
                .build();
                
        ReviewReport savedReport = reportRepository.save(report);
        review.getReports().add(savedReport);
        
        // Automatically put under moderation if reported
        review.setStatus("PENDING_MODERATION");
        reviewRepository.save(review);
        
        // Increment reported count in TrustScore
        TrustScore trustScore = trustScoreRepository.findByUserIdAndIsDeletedFalse(review.getRevieweeId())
                .orElseGet(() -> createDefaultTrustScore(review.getRevieweeId()));
        trustScore.setReportedReviewsCount(trustScore.getReportedReviewsCount() + 1);
        trustScoreRepository.save(trustScore);
        
        // Recalculate trust score due to new report
        updateRatingStatisticsAndTrustScore(review.getRevieweeId(), "Review reported for moderation");
        
        // Publish Event
        kafkaProducer.publishReviewReported(
                savedReport.getReview().getId(),
                savedReport.getReporterId(),
                savedReport.getReason()
        );
        
        return reviewMapper.toDto(savedReport);
    }

    @Override
    @Transactional
    public void likeReview(UUID reviewId, UUID userId) {
        log.info("Liking/unliking review: {}, user: {}", reviewId, userId);
        reviewRepository.findByIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));
                
        Optional<ReviewLike> existingLike = likeRepository.findByReviewIdAndUserIdAndIsDeletedFalse(reviewId, userId);
        if (existingLike.isPresent()) {
            // Unlike
            ReviewLike like = existingLike.get();
            like.setDeleted(true);
            likeRepository.save(like);
        } else {
            // Like
            ReviewLike like = ReviewLike.builder()
                    .reviewId(reviewId)
                    .userId(userId)
                    .build();
            likeRepository.save(like);
        }
    }

    @Override
    @Transactional
    public ReviewDisputeDto.ReviewDisputeResponse disputeReview(UUID reviewId, ReviewDisputeDto.Request request, UUID userId) {
        log.info("Disputing review: {}, user: {}", reviewId, userId);
        Review review = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));
                
        // Only the reviewee can dispute the review
        if (!review.getRevieweeId().equals(userId)) {
            throw new UnauthorizedReviewException("Only the recipient of the review can file a dispute");
        }
        
        ReviewDispute dispute = ReviewDispute.builder()
                .reviewId(reviewId)
                .userId(userId)
                .reason(request.reason())
                .status("PENDING")
                .build();
                
        ReviewDispute savedDispute = disputeRepository.save(dispute);
        
        // Update dispute count in trust score
        TrustScore trustScore = trustScoreRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseGet(() -> createDefaultTrustScore(userId));
        trustScore.setDisputeCount(trustScore.getDisputeCount() + 1);
        trustScoreRepository.save(trustScore);
        
        updateRatingStatisticsAndTrustScore(userId, "Dispute filed for review ID: " + reviewId);
        
        return reviewMapper.toDto(savedDispute);
    }

    @Override
    @Transactional
    public ReviewDisputeDto.ReviewDisputeResponse resolveDispute(UUID disputeId, ReviewDisputeDto.ResolveRequest request) {
        log.info("Resolving dispute: {}, status: {}", disputeId, request.status());
        ReviewDispute dispute = disputeRepository.findByIdAndIsDeletedFalse(disputeId)
                .orElseThrow(() -> new ReviewNotFoundException("Dispute not found with ID: " + disputeId));
                
        dispute.setStatus(request.status().toUpperCase());
        dispute.setResolution(request.resolution());
        ReviewDispute resolvedDispute = disputeRepository.save(dispute);
        
        Review review = reviewRepository.findByIdAndIsDeletedFalse(dispute.getReviewId())
                .orElseThrow(() -> new ReviewNotFoundException("Associated review not found"));
                
        if ("APPROVED".equalsIgnoreCase(request.status())) {
            // Dispute approved in favor of the reviewee -> we dismiss/delete the negative review
            review.setDeleted(true);
            reviewRepository.save(review);
            
            // Recalculate stats & trust score for reviewee
            updateRatingStatisticsAndTrustScore(review.getRevieweeId(), "Dispute approved, review dismissed");
        } else {
            // Dispute rejected -> negative review remains, but dispute is resolved.
            updateRatingStatisticsAndTrustScore(review.getRevieweeId(), "Dispute rejected");
        }
        
        return reviewMapper.toDto(resolvedDispute);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDto.ReviewResponse> searchReviews(
            UUID revieweeId, UUID reviewerId, UUID tripId, String role, String status,
            Integer minRating, Integer maxRating, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        
        Page<Review> reviews = reviewRepository.searchReviews(
                revieweeId, reviewerId, tripId, role, status, minRating, maxRating, startDate, endDate, pageable);
                
        return reviews.map(reviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRating(UUID userId) {
        return statisticsRepository.findByUserIdAndIsDeletedFalse(userId)
                .map(RatingStatistics::getAverageRating)
                .orElse(5.0);
    }

    @Override
    @Transactional(readOnly = true)
    public TrustScoreDto.TrustScoreResponse getTrustScore(UUID userId) {
        return trustScoreService.getTrustScore(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrustScoreDto.TrustScoreHistoryResponse> getTrustScoreHistory(UUID userId) {
        return trustScoreService.getReputationHistory(userId);
    }

    @Override
    @Transactional
    public void handleUserDeleted(UUID userId) {
        log.info("Processing user deletion for userId: {}", userId);
        
        // Soft delete all reviews written by or written for this user
        List<Review> reviews = reviewRepository.findByRevieweeIdAndIsDeletedFalse(userId);
        for (Review r : reviews) {
            r.setDeleted(true);
            reviewRepository.save(r);
        }
        
        // Soft delete trust score and stats
        trustScoreRepository.findByUserIdAndIsDeletedFalse(userId).ifPresent(score -> {
            score.setDeleted(true);
            trustScoreRepository.save(score);
        });
        
        statisticsRepository.findByUserIdAndIsDeletedFalse(userId).ifPresent(stats -> {
            stats.setDeleted(true);
            statisticsRepository.save(stats);
        });
    }

    private TrustScore createDefaultTrustScore(UUID userId) {
        TrustScore trustScore = TrustScore.builder()
                .userId(userId)
                .score(100)
                .averageRating(5.0)
                .completedTrips(0)
                .cancelledTrips(0)
                .reportedReviewsCount(0)
                .disputeCount(0)
                .build();
        return trustScoreRepository.save(trustScore);
    }

    private void updateRatingStatisticsAndTrustScore(UUID userId, String changeReason) {
        List<Review> reviews = reviewRepository.findByRevieweeIdAndIsDeletedFalse(userId);
        
        double avgCommunication = 0.0;
        double avgPunctuality = 0.0;
        double avgProfessionalism = 0.0;
        double avgVehicleCondition = 0.0;
        double avgCargoSafety = 0.0;
        double avgPaymentExperience = 0.0;
        double avgOverallExperience = 5.0;
        double totalAvgRating = 5.0;
        int activeReviewsCount = 0;
        
        if (!reviews.isEmpty()) {
            int commCount = 0, puncCount = 0, profCount = 0, vehCount = 0, cargoCount = 0, payCount = 0, overCount = 0;
            double commSum = 0, puncSum = 0, profSum = 0, vehSum = 0, cargoSum = 0, paySum = 0, overSum = 0;
            
            for (Review r : reviews) {
                if ("BLOCKED".equalsIgnoreCase(r.getStatus())) {
                    continue;
                }
                activeReviewsCount++;
                Rating rating = r.getRating();
                if (rating != null) {
                    if (rating.getCommunication() != null) { commSum += rating.getCommunication(); commCount++; }
                    if (rating.getPunctuality() != null) { puncSum += rating.getPunctuality(); puncCount++; }
                    if (rating.getProfessionalism() != null) { profSum += rating.getProfessionalism(); profCount++; }
                    if (rating.getVehicleCondition() != null) { vehSum += rating.getVehicleCondition(); vehCount++; }
                    if (rating.getCargoSafety() != null) { cargoSum += rating.getCargoSafety(); cargoCount++; }
                    if (rating.getPaymentExperience() != null) { paySum += rating.getPaymentExperience(); payCount++; }
                    if (rating.getOverallExperience() != null) { overSum += rating.getOverallExperience(); overCount++; }
                }
            }
            
            if (commCount > 0) avgCommunication = commSum / commCount;
            if (puncCount > 0) avgPunctuality = puncSum / puncCount;
            if (profCount > 0) avgProfessionalism = profSum / profCount;
            if (vehCount > 0) avgVehicleCondition = vehSum / vehCount;
            if (cargoCount > 0) avgCargoSafety = cargoSum / cargoCount;
            if (payCount > 0) avgPaymentExperience = paySum / payCount;
            if (overCount > 0) {
                avgOverallExperience = overSum / overCount;
                totalAvgRating = avgOverallExperience;
            }
        }
        
        RatingStatistics stats = statisticsRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseGet(() -> RatingStatistics.builder().userId(userId).build());
                
        stats.setAverageRating(totalAvgRating);
        stats.setTotalReviews(activeReviewsCount);
        stats.setAvgCommunication(avgCommunication);
        stats.setAvgPunctuality(avgPunctuality);
        stats.setAvgProfessionalism(avgProfessionalism);
        stats.setAvgVehicleCondition(avgVehicleCondition);
        stats.setAvgCargoSafety(avgCargoSafety);
        stats.setAvgPaymentExperience(avgPaymentExperience);
        stats.setAvgOverallExperience(avgOverallExperience);
        statisticsRepository.save(stats);
        
        // Update Trust Score properties
        TrustScore trustScore = trustScoreRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseGet(() -> createDefaultTrustScore(userId));
        trustScore.setAverageRating(totalAvgRating);
        trustScoreRepository.save(trustScore);
        
        // Let trustScoreService handle recalculation, history and events publishing
        trustScoreService.updateTrustScore(userId, changeReason);
    }
}
