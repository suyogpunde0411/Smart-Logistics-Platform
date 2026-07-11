package com.smartlogistics.analyticsservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReportRequestDto(
    String reportType,
    LocalDateTime startDate,
    LocalDateTime endDate,
    UUID driverId,
    UUID businessId,
    UUID truckId,
    UUID fleetOwnerId,
    String format
) {}
