package com.smartlogistics.adminservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import java.time.LocalDateTime;

public record FeatureFlagDto(
        UUID id,
        @NotBlank(message = "Flag key is mandatory")
        String flagKey,
        boolean isEnabled,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
