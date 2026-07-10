package com.smartlogistics.notificationservice.dto;

import java.util.UUID;

public class PreferenceDto {

    public record UpdateRequest(
            Boolean emailEnabled,
            Boolean smsEnabled,
            Boolean pushEnabled,
            Boolean marketingEnabled,
            Boolean systemAlertsEnabled,
            Boolean tripUpdatesEnabled,
            Boolean shipmentUpdatesEnabled
    ) {}

    public record Response(
            UUID id,
            UUID userId,
            boolean emailEnabled,
            boolean smsEnabled,
            boolean pushEnabled,
            boolean marketingEnabled,
            boolean systemAlertsEnabled,
            boolean tripUpdatesEnabled,
            boolean shipmentUpdatesEnabled
    ) {}
}
