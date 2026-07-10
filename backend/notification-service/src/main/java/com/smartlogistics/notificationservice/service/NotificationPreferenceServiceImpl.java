package com.smartlogistics.notificationservice.service;

import com.smartlogistics.notificationservice.dto.PreferenceDto;
import com.smartlogistics.notificationservice.entity.NotificationPreference;
import com.smartlogistics.notificationservice.mapper.NotificationMapper;
import com.smartlogistics.notificationservice.repository.NotificationPreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {

    private final NotificationPreferenceRepository preferenceRepository;
    private final NotificationMapper mapper;

    @Override
    public PreferenceDto.Response getPreferences(UUID userId) {
        NotificationPreference preference = preferenceRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseGet(() -> createDefaultPreference(userId));
        return mapper.toResponse(preference);
    }

    @Override
    @Transactional
    public PreferenceDto.Response updatePreferences(UUID userId, PreferenceDto.UpdateRequest request) {
        NotificationPreference preference = preferenceRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseGet(() -> NotificationPreference.builder().userId(userId).build());

        if (request.emailEnabled() != null) preference.setEmailEnabled(request.emailEnabled());
        if (request.smsEnabled() != null) preference.setSmsEnabled(request.smsEnabled());
        if (request.pushEnabled() != null) preference.setPushEnabled(request.pushEnabled());
        if (request.marketingEnabled() != null) preference.setMarketingEnabled(request.marketingEnabled());
        if (request.systemAlertsEnabled() != null) preference.setSystemAlertsEnabled(request.systemAlertsEnabled());
        if (request.tripUpdatesEnabled() != null) preference.setTripUpdatesEnabled(request.tripUpdatesEnabled());
        if (request.shipmentUpdatesEnabled() != null) preference.setShipmentUpdatesEnabled(request.shipmentUpdatesEnabled());

        NotificationPreference saved = preferenceRepository.save(preference);
        log.info("Successfully updated notification preferences for user ID: {}", userId);
        return mapper.toResponse(saved);
    }

    @Override
    public boolean isNotificationAllowed(UUID userId, String type, String channel) {
        NotificationPreference pref = preferenceRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseGet(() -> createDefaultPreference(userId));

        // Evaluate channel
        boolean channelAllowed = switch (channel.toUpperCase()) {
            case "EMAIL" -> pref.isEmailEnabled();
            case "SMS" -> pref.isSmsEnabled();
            case "PUSH" -> pref.isPushEnabled();
            case "IN_APP" -> true; // In-app is always enabled as system inbox
            case "WHATSAPP" -> pref.isSmsEnabled(); // Whatsapp follows SMS permission
            default -> false;
        };

        if (!channelAllowed) {
            log.debug("Channel {} is disabled for user ID: {}", channel, userId);
            return false;
        }

        // Evaluate type
        boolean typeAllowed = switch (type.toUpperCase()) {
            case "MARKETING" -> pref.isMarketingEnabled();
            case "SYSTEM" -> pref.isSystemAlertsEnabled();
            case "TRIP_UPDATE" -> pref.isTripUpdatesEnabled();
            case "SHIPMENT_UPDATE" -> pref.isShipmentUpdatesEnabled();
            default -> true; // custom alerts are allowed by default
        };

        if (!typeAllowed) {
            log.debug("Notification type {} is disabled for user ID: {}", type, userId);
        }

        return typeAllowed;
    }

    private NotificationPreference createDefaultPreference(UUID userId) {
        // By default, all notifications are enabled
        NotificationPreference pref = NotificationPreference.builder()
                .userId(userId)
                .emailEnabled(true)
                .smsEnabled(true)
                .pushEnabled(true)
                .marketingEnabled(true)
                .systemAlertsEnabled(true)
                .tripUpdatesEnabled(true)
                .shipmentUpdatesEnabled(true)
                .build();
        return preferenceRepository.save(pref);
    }
}
