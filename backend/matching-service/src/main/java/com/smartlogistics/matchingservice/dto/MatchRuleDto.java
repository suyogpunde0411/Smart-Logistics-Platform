package com.smartlogistics.matchingservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public final class MatchRuleDto {

    private MatchRuleDto() {}

    public record UpdateRequest(
            @NotNull(message = "Weight is required")
            @Min(value = 0, message = "Weight cannot be negative")
            @Max(value = 100, message = "Weight cannot exceed 100")
            Double weight,

            @NotNull(message = "Active status is required")
            Boolean active
    ) {}

    public record Response(
            String code,
            Double weight,
            boolean active,
            String description
    ) {}
}
