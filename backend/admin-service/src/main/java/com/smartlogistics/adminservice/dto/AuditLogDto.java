package com.smartlogistics.adminservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditLogDto(
        UUID id,
        String correlationId,
        String traceId,
        String serviceName,
        String message,
        String logLevel,
        LocalDateTime createdAt
) {}
