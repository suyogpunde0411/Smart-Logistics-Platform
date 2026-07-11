package com.smartlogistics.reviewservice.mapper;

import com.smartlogistics.reviewservice.entity.*;
import com.smartlogistics.reviewservice.dto.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    Rating toEntity(ReviewDto.ReviewRatingDto request);
    ReviewDto.ReviewRatingDto toDto(Rating rating);

    Review toEntity(ReviewDto.CreateRequest request);
    ReviewDto.ReviewResponse toDto(Review review);

    ReviewReplyDto.ReviewReplyResponse toDto(ReviewReply reply);
    ReviewReportDto.ReviewReportResponse toDto(ReviewReport report);
    ReviewDisputeDto.ReviewDisputeResponse toDto(ReviewDispute dispute);

    TrustScoreDto.TrustScoreResponse toDto(TrustScore trustScore);
    TrustScoreDto.TrustScoreResponse toResponse(TrustScore trustScore);

    TrustScoreDto.TrustScoreHistoryResponse toDto(ReputationHistory history);
    List<ReviewDto.ReviewResponse> toDtoList(List<Review> reviews);
    
    List<TrustScoreDto.TrustScoreHistoryResponse> toHistoryDtoList(List<ReputationHistory> histories);
    List<TrustScoreDto.TrustScoreHistoryResponse> toHistoryResponseList(List<ReputationHistory> histories);
}
