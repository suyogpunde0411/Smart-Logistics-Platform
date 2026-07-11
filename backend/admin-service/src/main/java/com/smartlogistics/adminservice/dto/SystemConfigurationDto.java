package com.smartlogistics.adminservice.dto;

import jakarta.validation.constraints.NotBlank;

public record SystemConfigurationDto(
        @NotBlank(message = "Config key is mandatory")
        String configKey,
        @NotBlank(message = "Config value is mandatory")
        String configValue,
        String description,
        String module
) {}
