package com.smartlogistics.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

public class ProfileDto {

    public record DriverProfileResponse(
            UUID id,
            String licenseNumber,
            LocalDate licenseExpiry,
            Integer experienceYears,
            String status
    ) {}

    public record DriverProfileUpdateRequest(
            @NotBlank(message = "License number is mandatory")
            @Size(max = 50)
            String licenseNumber,

            @NotNull(message = "License expiry is mandatory")
            LocalDate licenseExpiry,

            @NotNull(message = "Experience years is mandatory")
            Integer experienceYears
    ) {}

    public record BusinessProfileResponse(
            UUID id,
            String companyName,
            String taxId,
            String website
    ) {}

    public record BusinessProfileUpdateRequest(
            @NotBlank(message = "Company name is mandatory")
            @Size(max = 150)
            String companyName,

            @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$", message = "Invalid GST format")
            String taxId,

            @Size(max = 255)
            String website
    ) {}

    public record FleetOwnerProfileResponse(
            UUID id,
            String companyName,
            Integer fleetSize
    ) {}

    public record FleetOwnerProfileUpdateRequest(
            @NotBlank(message = "Company name is mandatory")
            @Size(max = 150)
            String companyName,

            @NotNull(message = "Fleet size is mandatory")
            Integer fleetSize
    ) {}
}
