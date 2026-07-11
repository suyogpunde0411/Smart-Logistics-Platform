package com.smartlogistics.adminservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record AnnouncementDto(
        UUID id,
        @NotBlank(message = "Title is mandatory")
        String title,
        @NotBlank(message = "Content is mandatory")
        String content,
        @NotNull(message = "Target audience is mandatory")
        String targetAudience,
        boolean isActive,
        LocalDateTime createdAt
) {}
