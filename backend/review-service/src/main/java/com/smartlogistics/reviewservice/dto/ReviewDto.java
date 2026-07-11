package com.smartlogistics.reviewservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReviewDto {

    public record ReviewRatingDto(
            @Min(1) @Max(5) Integer communication,
            @Min(1) @Max(5) Integer punctuality,
            @Min(1) @Max(5) Integer professionalism,
            @Min(1) @Max(5) Integer vehicleCondition,
            @Min(1) @Max(5) Integer cargoSafety,
            @Min(1) @Max(5) Integer paymentExperience,
            @NotNull(message = "Overall experience rating is required")
            @Min(1) @Max(5) Integer overallExperience
    ) {}

    public record CreateRequest(
            @NotNull(message = "Trip ID is required")
            UUID tripId,
            @NotNull(message = "Reviewee ID is required")
            UUID revieweeId,
            @NotBlank(message = "Reviewee role is required")
            String revieweeRole, // DRIVER, BUSINESS_OWNER, FLEET_OWNER
            String comment,
            @NotNull(message = "Rating is required")
            @Valid
            ReviewRatingDto rating
    ) {}

    public record UpdateRequest(
            String comment,
            @Valid
            ReviewRatingDto rating
    ) {}

    public record ReviewResponse(
            UUID id,
            UUID tripId,
            UUID reviewerId,
            UUID revieweeId,
            String revieweeRole,
            String comment,
            String status,
            ReviewRatingDto rating,
            List<ReviewReplyDto.ReviewReplyResponse> replies,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}