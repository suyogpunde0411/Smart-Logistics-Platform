package com.smartlogistics.userservice.dto;

import jakarta.validation.constraints.NotNull;

public class UserSettingsDto {

    public record UserSettingsResponse(
            boolean emailNotificationsEnabled,
            boolean smsNotificationsEnabled,
            boolean pushNotificationsEnabled,
            boolean twoFactorEnabled
    ) {}

    public record UserSettingsUpdateRequest(
            @NotNull(message = "Email notification preference is mandatory")
            Boolean emailNotificationsEnabled,

            @NotNull(message = "SMS notification preference is mandatory")
            Boolean smsNotificationsEnabled,

            @NotNull(message = "Push notification preference is mandatory")
            Boolean pushNotificationsEnabled,

            @NotNull(message = "Two-factor authentication preference is mandatory")
            Boolean twoFactorEnabled
    ) {}
}
