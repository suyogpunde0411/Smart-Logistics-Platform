package com.smartlogistics.reviewservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewDisputeDto {

    public record Request(
            @NotBlank(message = "Reason for dispute is required")
            String reason
    ) {}

    public record ResolveRequest(
            @NotBlank(message = "Resolution is required")
            String resolution,
            @NotBlank(message = "Status (APPROVED/REJECTED) is required")
            String status
    ) {}

    public record ReviewDisputeResponse(
            UUID id,
            UUID reviewId,
            UUID userId,
            String reason,
            String status,
            String resolution,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
