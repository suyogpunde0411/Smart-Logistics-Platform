package com.smartlogistics.shared.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TruckDTO {

    private TruckDTO() {}

    public record RegisterRequest(
            @NotBlank(message = "Registration number is required")
            @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$", message = "Invalid Indian registration number format (e.g. MH12AB1234)")
            String registrationNumber,

            @NotNull(message = "Owner ID is required")
            UUID ownerId,

            @NotNull(message = "Max weight capacity is required")
            @Positive(message = "Capacity must be greater than zero")
            Double maxWeight,

            @NotNull(message = "Max volume capacity is required")
            @Positive(message = "Capacity must be greater than zero")
            Double maxVolume
    ) {}

    public record UpdateRequest(
            @NotNull(message = "Max weight capacity is required")
            @Positive(message = "Capacity must be greater than zero")
            Double maxWeight,

            @NotNull(message = "Max volume capacity is required")
            @Positive(message = "Capacity must be greater than zero")
            Double maxVolume
    ) {}

    public record Response(
            UUID id,
            String registrationNumber,
            UUID ownerId,
            String status,
            CapacityDto capacity,
            AvailabilityDto availability,
            LocationDto location,
            InsuranceDto insurance,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    public record CapacityDto(
            Double maxWeight,
            Double maxVolume
    ) {}

    public record AvailabilityDto(
            String status,
            boolean active
    ) {}

    public record AvailabilityToggle(
            @NotNull(message = "Active status is required")
            Boolean active,
            @NotBlank(message = "Status code is required")
            String status
    ) {}

    public record LocationDto(
            @NotNull(message = "Latitude is required")
            @Min(value = -90, message = "Latitude must be between -90 and 90")
            @Max(value = 90, message = "Latitude must be between -90 and 90")
            Double latitude,

            @NotNull(message = "Longitude is required")
            @Min(value = -180, message = "Longitude must be between -180 and 180")
            @Max(value = 180, message = "Longitude must be between -180 and 180")
            Double longitude,

            Double speed,
            Double heading,
            Double accuracy,
            LocalDateTime timestamp
    ) {}

    public record DocumentDto(
            UUID id,
            String type,
            String documentNumber,
            LocalDate expiryDate,
            String url,
            String status
    ) {}

    public record DocumentUploadRequest(
            @NotBlank(message = "Document type is required")
            String type, // RC_BOOK, FITNESS_CERTIFICATE, PERMIT, POLLUTION_CERTIFICATE

            @NotBlank(message = "Document number is required")
            String documentNumber,

            LocalDate expiryDate,

            @NotBlank(message = "Document file URL is required")
            String url
    ) {}

    public record InsuranceDto(
            UUID id,
            String policyNumber,
            String provider,
            LocalDate expiryDate,
            Double insuredAmount,
            String url
    ) {}

    public record InsuranceCreateRequest(
            @NotBlank(message = "Policy number is required")
            String policyNumber,

            @NotBlank(message = "Insurance provider is required")
            String provider,

            @NotNull(message = "Expiry date is required")
            LocalDate expiryDate,

            @Positive(message = "Insured amount must be positive")
            Double insuredAmount,

            @NotBlank(message = "Insurance copy URL is required")
            String url
    ) {}

    public record MaintenanceDto(
            UUID id,
            LocalDate maintenanceDate,
            String description,
            Double cost,
            String status,
            String performedBy
    ) {}

    public record MaintenanceCreateRequest(
            @NotNull(message = "Maintenance date is required")
            LocalDate maintenanceDate,

            String description,

            @NotNull(message = "Cost is required")
            @Positive(message = "Cost must be positive")
            Double cost,

            @NotBlank(message = "Status is required")
            String status, // SCHEDULED, IN_PROGRESS, COMPLETED

            String performedBy
    ) {}

    public record ImageDto(
            UUID id,
            String url,
            String contentType
    ) {}

    public record ImageUploadRequest(
            @NotBlank(message = "Image URL is required")
            String url,
            String contentType
    ) {}

    public record StatusHistoryDto(
            String oldStatus,
            String newStatus,
            UUID changedBy,
            LocalDateTime changedAt,
            String remarks
    ) {}
}
