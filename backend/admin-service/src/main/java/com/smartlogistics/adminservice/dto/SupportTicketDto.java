package com.smartlogistics.adminservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

public record SupportTicketDto(
        UUID id,
        UUID userId,
        @NotBlank(message = "Subject is mandatory")
        String subject,
        @NotBlank(message = "Description is mandatory")
        String description,
        String status,
        String priority,
        String resolution,
        UUID resolvedBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
