package com.smartlogistics.reviewservice;

import com.smartlogistics.common.client.UserFeignClient;
import com.smartlogistics.reviewservice.client.TrackingFeignClient;
import com.smartlogistics.reviewservice.dto.ReviewDto;
import com.smartlogistics.reviewservice.entity.Rating;
import com.smartlogistics.reviewservice.entity.Review;
import com.smartlogistics.reviewservice.events.ReviewKafkaProducer;
import com.smartlogistics.reviewservice.exception.DuplicateReviewException;
import com.smartlogistics.reviewservice.exception.TripNotCompletedException;
import com.smartlogistics.reviewservice.exception.UnauthorizedReviewException;
import com.smartlogistics.reviewservice.mapper.ReviewMapper;
import com.smartlogistics.reviewservice.repository.*;
import com.smartlogistics.reviewservice.service.ReviewServiceImpl;
import com.smartlogistics.reviewservice.service.TrustScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private RatingRepository ratingRepository;
    @Mock private ReviewReplyRepository replyRepository;
    @Mock private ReviewReportRepository reportRepository;
    @Mock private ReviewLikeRepository likeRepository;
    @Mock private ReviewDisputeRepository disputeRepository;
    @Mock private RatingStatisticsRepository statsRepository;
    @Mock private TrustScoreRepository trustScoreRepository;
    @Mock private TrustScoreService trustScoreService;
    @Mock private ReviewMapper mapper;
    @Mock private ReviewKafkaProducer kafkaProducer;
    @Mock private TrackingFeignClient trackingClient;
    @Mock private UserFeignClient userClient;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private UUID reviewerId;
    private UUID revieweeId;
    private UUID tripId;
    private ReviewDto.CreateRequest createRequest;

    @BeforeEach
    public void setUp() {
        reviewerId = UUID.randomUUID();
        revieweeId = UUID.randomUUID();
        tripId = UUID.randomUUID();
        
        ReviewDto.ReviewRatingDto ratingDto = new ReviewDto.ReviewRatingDto(5, 5, 5, 5, 5, 5, 5);
        createRequest = new ReviewDto.CreateRequest(tripId, revieweeId, "DRIVER", "Good driver", ratingDto);
    }

    @Test
    public void testCreateReview_TripNotCompleted_ThrowsException() {
        // User profile verification passes
        when(userClient.getUser(revieweeId)).thenReturn(new UserFeignClient.InternalUserResponse(revieweeId, "a@b.com", "123", "fn", "ln", "ACTIVE"));

        // Trip is enroute (not completed)
        TrackingFeignClient.InternalTripResponse tripResponse = new TrackingFeignClient.InternalTripResponse(
                tripId, UUID.randomUUID(), UUID.randomUUID(), revieweeId, reviewerId, "EN_ROUTE"
        );
        when(trackingClient.getTrip(tripId)).thenReturn(tripResponse);

        assertThrows(TripNotCompletedException.class, () -> {
            reviewService.createReview(reviewerId, createRequest);
        });
    }

    @Test
    public void testCreateReview_DuplicateSubmission_ThrowsException() {
        when(userClient.getUser(revieweeId)).thenReturn(new UserFeignClient.InternalUserResponse(revieweeId, "a@b.com", "123", "fn", "ln", "ACTIVE"));

        TrackingFeignClient.InternalTripResponse tripResponse = new TrackingFeignClient.InternalTripResponse(
                tripId, UUID.randomUUID(), UUID.randomUUID(), revieweeId, reviewerId, "COMPLETED"
        );
        when(trackingClient.getTrip(tripId)).thenReturn(tripResponse);
        when(reviewRepository.existsByTripIdAndRevieweeRoleAndIsDeletedFalse(tripId, "DRIVER")).thenReturn(true);

        assertThrows(DuplicateReviewException.class, () -> {
            reviewService.createReview(reviewerId, createRequest);
        });
    }

    @Test
    public void testCreateReview_NonParticipantReviewer_ThrowsException() {
        when(userClient.getUser(revieweeId)).thenReturn(new UserFeignClient.InternalUserResponse(revieweeId, "a@b.com", "123", "fn", "ln", "ACTIVE"));

        // Driver and business owner IDs on trip don't match our reviewerId
        TrackingFeignClient.InternalTripResponse tripResponse = new TrackingFeignClient.InternalTripResponse(
                tripId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "COMPLETED"
        );
        when(trackingClient.getTrip(tripId)).thenReturn(tripResponse);

        assertThrows(UnauthorizedReviewException.class, () -> {
            reviewService.createReview(reviewerId, createRequest);
        });
    }

    @Test
    public void testUpdateReview_ImmutableAfter24Hours_ThrowsException() {
        UUID reviewId = UUID.randomUUID();
        
        Review review = Review.builder()
                .tripId(tripId)
                .reviewerId(reviewerId)
                .revieweeId(revieweeId)
                .revieweeRole("DRIVER")
                .comment("Old comment")
                .status("APPROVED")
                .build();
        review.setCreatedAt(LocalDateTime.now().minusHours(25)); // 25 hours old

        when(reviewRepository.findByIdAndIsDeletedFalse(reviewId)).thenReturn(Optional.of(review));

        ReviewDto.UpdateRequest updateRequest = new ReviewDto.UpdateRequest("New comment", new ReviewDto.ReviewRatingDto(5, 5, 5, 5, 5, 5, 5));

        assertThrows(UnauthorizedReviewException.class, () -> {
            reviewService.updateReview(reviewerId, reviewId, updateRequest);
        });
    }
}
