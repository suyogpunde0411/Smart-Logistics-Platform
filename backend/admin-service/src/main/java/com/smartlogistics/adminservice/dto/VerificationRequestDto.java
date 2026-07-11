package com.smartlogistics.adminservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record VerificationRequestDto(
        UUID id,
        UUID entityId,
        String entityType,
        String status,
        String comments,
        UUID reviewedBy,
        LocalDateTime reviewedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
