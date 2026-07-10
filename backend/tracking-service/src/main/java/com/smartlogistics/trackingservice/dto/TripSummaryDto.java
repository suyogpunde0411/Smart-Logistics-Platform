package com.smartlogistics.trackingservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TripSummaryDto {

    public record Response(
            UUID id,
            LocalDateTime startActualTime,
            LocalDateTime endActualTime,
            Double durationHours,
            Double totalDistanceKm,
            Double averageSpeedKmh,
            Double totalFuelVolumeLiters,
            Double totalFuelCost,
            Integer delayCount,
            Double totalDelayMinutes,
            Integer restStopCount,
            Double totalRestMinutes
    ) {}
}
