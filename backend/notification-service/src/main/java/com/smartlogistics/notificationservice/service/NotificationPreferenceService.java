package com.smartlogistics.notificationservice.service;

import com.smartlogistics.notificationservice.dto.PreferenceDto;
import java.util.UUID;

public interface NotificationPreferenceService {
    PreferenceDto.Response getPreferences(UUID userId);
    PreferenceDto.Response updatePreferences(UUID userId, PreferenceDto.UpdateRequest request);
    boolean isNotificationAllowed(UUID userId, String type, String channel);
}
