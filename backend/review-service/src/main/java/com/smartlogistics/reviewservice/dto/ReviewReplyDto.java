package com.smartlogistics.reviewservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewReplyDto {

    public record Request(
            @NotBlank(message = "Reply message is required")
            String message
    ) {}

    public record ReviewReplyResponse(
            UUID id,
            UUID reviewId,
            UUID replierId,
            String message,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
