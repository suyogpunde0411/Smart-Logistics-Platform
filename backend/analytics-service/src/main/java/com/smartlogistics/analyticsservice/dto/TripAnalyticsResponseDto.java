package com.smartlogistics.analyticsservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TripAnalyticsResponseDto(
    UUID tripId,
    UUID shipmentId,
    UUID driverId,
    UUID truckId,
    UUID businessId,
    String status,
    Double distanceKm,
    Double durationMinutes,
    Double fuelConsumedLiters,
    Double carbonSavingsKg,
    Boolean isLate,
    Boolean isEmpty,
    LocalDateTime createdAt
) {}
