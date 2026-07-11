package com.smartlogistics.reviewservice.mapper;

import com.smartlogistics.reviewservice.dto.ReviewDisputeDto;
import com.smartlogistics.reviewservice.dto.ReviewDto;
import com.smartlogistics.reviewservice.dto.ReviewReplyDto;
import com.smartlogistics.reviewservice.dto.ReviewReportDto;
import com.smartlogistics.reviewservice.dto.TrustScoreDto;
import com.smartlogistics.reviewservice.entity.Rating;
import com.smartlogistics.reviewservice.entity.ReputationHistory;
import com.smartlogistics.reviewservice.entity.Review;
import com.smartlogistics.reviewservice.entity.ReviewDispute;
import com.smartlogistics.reviewservice.entity.ReviewReply;
import com.smartlogistics.reviewservice.entity.ReviewReport;
import com.smartlogistics.reviewservice.entity.TrustScore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T20:14:59+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Rating toEntity(ReviewDto.ReviewRatingDto request) {
        if ( request == null ) {
            return null;
        }

        Rating.RatingBuilder rating = Rating.builder();

        rating.communication( request.communication() );
        rating.punctuality( request.punctuality() );
        rating.professionalism( request.professionalism() );
        rating.vehicleCondition( request.vehicleCondition() );
        rating.cargoSafety( request.cargoSafety() );
        rating.paymentExperience( request.paymentExperience() );
        rating.overallExperience( request.overallExperience() );

        return rating.build();
    }

    @Override
    public ReviewDto.ReviewRatingDto toDto(Rating rating) {
        if ( rating == null ) {
            return null;
        }

        Integer communication = null;
        Integer punctuality = null;
        Integer professionalism = null;
        Integer vehicleCondition = null;
        Integer cargoSafety = null;
        Integer paymentExperience = null;
        Integer overallExperience = null;

        communication = rating.getCommunication();
        punctuality = rating.getPunctuality();
        professionalism = rating.getProfessionalism();
        vehicleCondition = rating.getVehicleCondition();
        cargoSafety = rating.getCargoSafety();
        paymentExperience = rating.getPaymentExperience();
        overallExperience = rating.getOverallExperience();

        ReviewDto.ReviewRatingDto reviewRatingDto = new ReviewDto.ReviewRatingDto( communication, punctuality, professionalism, vehicleCondition, cargoSafety, paymentExperience, overallExperience );

        return reviewRatingDto;
    }

    @Override
    public Review toEntity(ReviewDto.CreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.tripId( request.tripId() );
        review.revieweeId( request.revieweeId() );
        review.revieweeRole( request.revieweeRole() );
        review.comment( request.comment() );
        review.rating( toEntity( request.rating() ) );

        return review.build();
    }

    @Override
    public ReviewDto.ReviewResponse toDto(Review review) {
        if ( review == null ) {
            return null;
        }

        UUID id = null;
        UUID tripId = null;
        UUID reviewerId = null;
        UUID revieweeId = null;
        String revieweeRole = null;
        String comment = null;
        String status = null;
        ReviewDto.ReviewRatingDto rating = null;
        List<ReviewReplyDto.ReviewReplyResponse> replies = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = review.getId();
        tripId = review.getTripId();
        reviewerId = review.getReviewerId();
        revieweeId = review.getRevieweeId();
        revieweeRole = review.getRevieweeRole();
        comment = review.getComment();
        status = review.getStatus();
        rating = toDto( review.getRating() );
        replies = reviewReplyListToReviewReplyResponseList( review.getReplies() );
        createdAt = review.getCreatedAt();
        updatedAt = review.getUpdatedAt();

        ReviewDto.ReviewResponse reviewResponse = new ReviewDto.ReviewResponse( id, tripId, reviewerId, revieweeId, revieweeRole, comment, status, rating, replies, createdAt, updatedAt );

        return reviewResponse;
    }

    @Override
    public ReviewReplyDto.ReviewReplyResponse toDto(ReviewReply reply) {
        if ( reply == null ) {
            return null;
        }

        UUID id = null;
        UUID replierId = null;
        String message = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = reply.getId();
        replierId = reply.getReplierId();
        message = reply.getMessage();
        createdAt = reply.getCreatedAt();
        updatedAt = reply.getUpdatedAt();

        UUID reviewId = null;

        ReviewReplyDto.ReviewReplyResponse reviewReplyResponse = new ReviewReplyDto.ReviewReplyResponse( id, reviewId, replierId, message, createdAt, updatedAt );

        return reviewReplyResponse;
    }

    @Override
    public ReviewReportDto.ReviewReportResponse toDto(ReviewReport report) {
        if ( report == null ) {
            return null;
        }

        UUID id = null;
        UUID reporterId = null;
        String reason = null;
        String status = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = report.getId();
        reporterId = report.getReporterId();
        reason = report.getReason();
        status = report.getStatus();
        createdAt = report.getCreatedAt();
        updatedAt = report.getUpdatedAt();

        UUID reviewId = null;

        ReviewReportDto.ReviewReportResponse reviewReportResponse = new ReviewReportDto.ReviewReportResponse( id, reviewId, reporterId, reason, status, createdAt, updatedAt );

        return reviewReportResponse;
    }

    @Override
    public ReviewDisputeDto.ReviewDisputeResponse toDto(ReviewDispute dispute) {
        if ( dispute == null ) {
            return null;
        }

        UUID id = null;
        UUID reviewId = null;
        UUID userId = null;
        String reason = null;
        String status = null;
        String resolution = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = dispute.getId();
        reviewId = dispute.getReviewId();
        userId = dispute.getUserId();
        reason = dispute.getReason();
        status = dispute.getStatus();
        resolution = dispute.getResolution();
        createdAt = dispute.getCreatedAt();
        updatedAt = dispute.getUpdatedAt();

        ReviewDisputeDto.ReviewDisputeResponse reviewDisputeResponse = new ReviewDisputeDto.ReviewDisputeResponse( id, reviewId, userId, reason, status, resolution, createdAt, updatedAt );

        return reviewDisputeResponse;
    }

    @Override
    public TrustScoreDto.TrustScoreResponse toDto(TrustScore trustScore) {
        if ( trustScore == null ) {
            return null;
        }

        UUID id = null;
        UUID userId = null;
        Integer score = null;
        Double averageRating = null;
        Integer completedTrips = null;
        Integer cancelledTrips = null;
        Integer reportedReviewsCount = null;
        Integer disputeCount = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = trustScore.getId();
        userId = trustScore.getUserId();
        score = trustScore.getScore();
        averageRating = trustScore.getAverageRating();
        completedTrips = trustScore.getCompletedTrips();
        cancelledTrips = trustScore.getCancelledTrips();
        reportedReviewsCount = trustScore.getReportedReviewsCount();
        disputeCount = trustScore.getDisputeCount();
        createdAt = trustScore.getCreatedAt();
        updatedAt = trustScore.getUpdatedAt();

        TrustScoreDto.TrustScoreResponse trustScoreResponse = new TrustScoreDto.TrustScoreResponse( id, userId, score, averageRating, completedTrips, cancelledTrips, reportedReviewsCount, disputeCount, createdAt, updatedAt );

        return trustScoreResponse;
    }

    @Override
    public TrustScoreDto.TrustScoreResponse toResponse(TrustScore trustScore) {
        if ( trustScore == null ) {
            return null;
        }

        UUID id = null;
        UUID userId = null;
        Integer score = null;
        Double averageRating = null;
        Integer completedTrips = null;
        Integer cancelledTrips = null;
        Integer reportedReviewsCount = null;
        Integer disputeCount = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = trustScore.getId();
        userId = trustScore.getUserId();
        score = trustScore.getScore();
        averageRating = trustScore.getAverageRating();
        completedTrips = trustScore.getCompletedTrips();
        cancelledTrips = trustScore.getCancelledTrips();
        reportedReviewsCount = trustScore.getReportedReviewsCount();
        disputeCount = trustScore.getDisputeCount();
        createdAt = trustScore.getCreatedAt();
        updatedAt = trustScore.getUpdatedAt();

        TrustScoreDto.TrustScoreResponse trustScoreResponse = new TrustScoreDto.TrustScoreResponse( id, userId, score, averageRating, completedTrips, cancelledTrips, reportedReviewsCount, disputeCount, createdAt, updatedAt );

        return trustScoreResponse;
    }

    @Override
    public TrustScoreDto.TrustScoreHistoryResponse toDto(ReputationHistory history) {
        if ( history == null ) {
            return null;
        }

        UUID id = null;
        UUID userId = null;
        Integer oldScore = null;
        Integer newScore = null;
        String reason = null;
        LocalDateTime createdAt = null;

        id = history.getId();
        userId = history.getUserId();
        oldScore = history.getOldScore();
        newScore = history.getNewScore();
        reason = history.getReason();
        createdAt = history.getCreatedAt();

        TrustScoreDto.TrustScoreHistoryResponse trustScoreHistoryResponse = new TrustScoreDto.TrustScoreHistoryResponse( id, userId, oldScore, newScore, reason, createdAt );

        return trustScoreHistoryResponse;
    }

    @Override
    public List<ReviewDto.ReviewResponse> toDtoList(List<Review> reviews) {
        if ( reviews == null ) {
            return null;
        }

        List<ReviewDto.ReviewResponse> list = new ArrayList<ReviewDto.ReviewResponse>( reviews.size() );
        for ( Review review : reviews ) {
            list.add( toDto( review ) );
        }

        return list;
    }

    @Override
    public List<TrustScoreDto.TrustScoreHistoryResponse> toHistoryDtoList(List<ReputationHistory> histories) {
        if ( histories == null ) {
            return null;
        }

        List<TrustScoreDto.TrustScoreHistoryResponse> list = new ArrayList<TrustScoreDto.TrustScoreHistoryResponse>( histories.size() );
        for ( ReputationHistory reputationHistory : histories ) {
            list.add( toDto( reputationHistory ) );
        }

        return list;
    }

    @Override
    public List<TrustScoreDto.TrustScoreHistoryResponse> toHistoryResponseList(List<ReputationHistory> histories) {
        if ( histories == null ) {
            return null;
        }

        List<TrustScoreDto.TrustScoreHistoryResponse> list = new ArrayList<TrustScoreDto.TrustScoreHistoryResponse>( histories.size() );
        for ( ReputationHistory reputationHistory : histories ) {
            list.add( toDto( reputationHistory ) );
        }

        return list;
    }

    protected List<ReviewReplyDto.ReviewReplyResponse> reviewReplyListToReviewReplyResponseList(List<ReviewReply> list) {
        if ( list == null ) {
            return null;
        }

        List<ReviewReplyDto.ReviewReplyResponse> list1 = new ArrayList<ReviewReplyDto.ReviewReplyResponse>( list.size() );
        for ( ReviewReply reviewReply : list ) {
            list1.add( toDto( reviewReply ) );
        }

        return list1;
    }
}
