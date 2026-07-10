package com.smartlogistics.shipmentservice.dto;

import com.smartlogistics.shared.enums.ShipmentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class ShipmentDto {

    private ShipmentDto() {}

    // ─── Requests ──────────────────────────────────────────────────────────────

    public record CreateRequest(
            @NotNull(message = "Business owner ID is required")
            UUID businessOwnerId,

            @NotBlank(message = "Origin address is required")
            @Size(max = 500)
            String originAddress,

            @NotNull(message = "Origin latitude is required")
            @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
            Double originLatitude,

            @NotNull(message = "Origin longitude is required")
            @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
            Double originLongitude,

            @NotBlank(message = "Destination address is required")
            @Size(max = 500)
            String destinationAddress,

            @NotNull(message = "Destination latitude is required")
            @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
            Double destinationLatitude,

            @NotNull(message = "Destination longitude is required")
            @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
            Double destinationLongitude,

            @NotBlank(message = "Cargo type is required")
            String cargoType,

            @NotNull(message = "Total weight is required")
            @Positive(message = "Weight must be greater than zero")
            Double totalWeight,

            Double totalVolume,

            String weightUnit,

            String volumeUnit,

            @Size(max = 1000)
            String description,

            Double budgetAmount,

            String budgetCurrency,

            String requiredTruckType,

            LocalDateTime expiresAt,

            @Valid
            PickupScheduleRequest pickupDetails,

            @Valid
            DropScheduleRequest dropDetails,

            @Valid
            DimensionRequest dimension,

            List<@Valid ItemRequest> items,

            List<String> requirementTypes
    ) {}

    public record UpdateRequest(
            String originAddress,
            Double originLatitude,
            Double originLongitude,
            String destinationAddress,
            Double destinationLatitude,
            Double destinationLongitude,
            String cargoType,
            @Positive(message = "Weight must be greater than zero")
            Double totalWeight,
            Double totalVolume,
            String description,
            Double budgetAmount,
            String budgetCurrency,
            String requiredTruckType,
            LocalDateTime expiresAt
    ) {}

    public record StatusUpdateRequest(
            @NotNull(message = "Status is required")
            ShipmentStatus newStatus,
            String remarks
    ) {}

    public record PickupScheduleRequest(
            @NotBlank(message = "Pickup address is required")
            String address,

            @NotNull @DecimalMin("-90.0") @DecimalMax("90.0")
            Double latitude,

            @NotNull @DecimalMin("-180.0") @DecimalMax("180.0")
            Double longitude,

            String city,
            String state,
            String pinCode,
            String contactName,
            String contactPhone,
            LocalDateTime scheduledAt,
            String specialInstructions
    ) {}

    public record DropScheduleRequest(
            @NotBlank(message = "Drop address is required")
            String address,

            @NotNull @DecimalMin("-90.0") @DecimalMax("90.0")
            Double latitude,

            @NotNull @DecimalMin("-180.0") @DecimalMax("180.0")
            Double longitude,

            String city,
            String state,
            String pinCode,
            String contactName,
            String contactPhone,
            LocalDateTime scheduledAt,
            String specialInstructions
    ) {}

    public record DimensionRequest(
            @Positive Double lengthCm,
            @Positive Double widthCm,
            @Positive Double heightCm,
            String dimensionUnit
    ) {}

    public record ItemRequest(
            @NotBlank(message = "Item name is required")
            String name,

            @NotNull @Positive
            Integer quantity,

            @Positive Double weight,
            Double volume,
            String unit,
            String description,
            Double value,
            String currency
    ) {}

    public record DocumentUploadRequest(
            @NotBlank(message = "Document type is required")
            String documentType,

            @NotBlank(message = "Document number is required")
            String documentNumber,

            String fileUrl,

            String fileName,
            String contentType,
            Long fileSizeBytes,
            java.time.LocalDate expiryDate
    ) {}

    public record ImageUploadRequest(
            String fileUrl,

            String fileName,
            String contentType,
            Long fileSizeBytes,
            String caption
    ) {}

    public record PricingUpdateRequest(
            @PositiveOrZero
            Double baseRate,
            @PositiveOrZero
            Double distanceCharge,
            @PositiveOrZero
            Double weightCharge,
            @PositiveOrZero
            Double insuranceCharge,
            @PositiveOrZero
            Double taxAmount,
            @PositiveOrZero
            Double totalAmount,
            String currency,
            String pricingStatus
    ) {}

    // ─── Responses ─────────────────────────────────────────────────────────────

    public record Response(
            UUID id,
            String trackingNumber,
            UUID businessOwnerId,
            String originAddress,
            Double originLatitude,
            Double originLongitude,
            String destinationAddress,
            Double destinationLatitude,
            Double destinationLongitude,
            String status,
            String cargoType,
            Double totalWeight,
            Double totalVolume,
            String weightUnit,
            String volumeUnit,
            String description,
            Double budgetAmount,
            String budgetCurrency,
            String requiredTruckType,
            LocalDateTime expiresAt,
            PickupResponse pickupDetails,
            DropResponse dropDetails,
            PricingResponse pricing,
            DimensionResponse dimension,
            List<ItemResponse> items,
            List<DocumentResponse> documents,
            List<ImageResponse> images,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    public record PickupResponse(
            UUID id,
            String address,
            Double latitude,
            Double longitude,
            String city,
            String state,
            String pinCode,
            String contactName,
            String contactPhone,
            LocalDateTime scheduledAt,
            LocalDateTime completedAt,
            String specialInstructions
    ) {}

    public record DropResponse(
            UUID id,
            String address,
            Double latitude,
            Double longitude,
            String city,
            String state,
            String pinCode,
            String contactName,
            String contactPhone,
            LocalDateTime scheduledAt,
            LocalDateTime completedAt,
            String specialInstructions
    ) {}

    public record PricingResponse(
            UUID id,
            Double baseRate,
            Double distanceCharge,
            Double weightCharge,
            Double insuranceCharge,
            Double taxAmount,
            Double totalAmount,
            String currency,
            String pricingStatus
    ) {}

    public record DimensionResponse(
            UUID id,
            Double lengthCm,
            Double widthCm,
            Double heightCm,
            Double volumeCbm,
            String dimensionUnit
    ) {}

    public record ItemResponse(
            UUID id,
            String name,
            Integer quantity,
            Double weight,
            Double volume,
            String unit,
            String description,
            Double value,
            String currency
    ) {}

    public record DocumentResponse(
            UUID id,
            String documentType,
            String documentNumber,
            String fileUrl,
            String fileName,
            String contentType,
            Long fileSizeBytes,
            java.time.LocalDate expiryDate,
            String status,
            LocalDateTime createdAt
    ) {}

    public record ImageResponse(
            UUID id,
            String fileUrl,
            String fileName,
            String contentType,
            Long fileSizeBytes,
            String caption,
            LocalDateTime createdAt
    ) {}

    public record StatusHistoryResponse(
            UUID id,
            String oldStatus,
            String newStatus,
            LocalDateTime changedAt,
            UUID changedBy,
            String remarks
    ) {}

    public record TrackingResponse(
            String trackingNumber,
            String status,
            PickupResponse pickup,
            DropResponse drop,
            List<StatusHistoryResponse> history
    ) {}

    public record CategoryResponse(
            UUID id,
            String code,
            String displayName,
            String description,
            boolean active,
            Boolean requiresSpecialHandling,
            Boolean requiresRefrigeration,
            Boolean isHazardous
    ) {}
}
