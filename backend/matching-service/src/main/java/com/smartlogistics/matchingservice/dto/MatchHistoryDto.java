package com.smartlogistics.matchingservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatchHistoryDto(
        UUID matchId,
        UUID shipmentId,
        UUID truckId,
        UUID driverId,
        UUID businessId,
        Double overallScore,
        Double amount,
        String status, // ACCEPTED, REJECTED, EXPIRED, COMPLETED
        LocalDateTime timestamp,
        String details
) {}
