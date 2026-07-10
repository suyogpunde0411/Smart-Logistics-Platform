package com.smartlogistics.matchingservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public final class BidDto {

    private BidDto() {}

    public record CreateRequest(
            @NotNull(message = "Match result ID is required")
            UUID matchResultId,

            @NotNull(message = "Bid amount is required")
            @DecimalMin(value = "0.01", message = "Bid amount must be positive")
            Double amount,

            @NotBlank(message = "Currency is required")
            String currency,

            String message,

            Integer ttlMinutes
    ) {}

    public record Response(
            UUID id,
            UUID matchResultId,
            UUID shipmentId,
            UUID truckId,
            UUID driverId,
            UUID businessId,
            Double amount,
            String currency,
            String status,
            String message,
            LocalDateTime expiresAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    public record RejectRequest(
            String reason
    ) {}
}
