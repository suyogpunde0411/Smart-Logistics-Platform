package com.smartlogistics.reviewservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewReportDto {

    public record Request(
            @NotBlank(message = "Reason for reporting is required")
            String reason
    ) {}

    public record ReviewReportResponse(
            UUID id,
            UUID reviewId,
            UUID reporterId,
            String reason,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
