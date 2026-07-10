package com.smartlogistics.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class TemplateDto {

    public record CreateRequest(
            @NotBlank(message = "Template name is required")
            String name,

            @NotBlank(message = "Title template is required")
            String titleTemplate,

            @NotBlank(message = "Body template is required")
            String bodyTemplate,

            @NotBlank(message = "Channel is required")
            String channel,

            @NotBlank(message = "Notification type is required")
            String type
    ) {}

    public record Response(
            UUID id,
            String name,
            String titleTemplate,
            String bodyTemplate,
            String channel,
            String type
    ) {}
}
