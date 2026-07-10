package com.smartlogistics.shared.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShipmentDTO(
        UUID id,
        String trackingNumber,
        UUID customerId,
        LocationDTO origin,
        LocationDTO destination,
        String status,
        Double weight,
        Double volume,
        LocalDateTime estimatedDeliveryDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
