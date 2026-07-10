package com.smartlogistics.notificationservice;

import com.smartlogistics.notificationservice.dto.PreferenceDto;
import com.smartlogistics.notificationservice.entity.NotificationPreference;
import com.smartlogistics.notificationservice.mapper.NotificationMapper;
import com.smartlogistics.notificationservice.repository.NotificationPreferenceRepository;
import com.smartlogistics.notificationservice.service.NotificationPreferenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PreferenceServiceTest {

    @Mock
    private NotificationPreferenceRepository preferenceRepository;

    @Mock
    private NotificationMapper mapper;

    @InjectMocks
    private NotificationPreferenceServiceImpl preferenceService;

    private UUID userId;
    private NotificationPreference preference;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        preference = NotificationPreference.builder()
                .userId(userId)
                .emailEnabled(true)
                .smsEnabled(false)
                .pushEnabled(true)
                .marketingEnabled(false)
                .systemAlertsEnabled(true)
                .tripUpdatesEnabled(true)
                .shipmentUpdatesEnabled(true)
                .build();
    }

    @Test
    public void testIsNotificationAllowed_EmailSystem_True() {
        when(preferenceRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(preference));

        boolean result = preferenceService.isNotificationAllowed(userId, "SYSTEM", "EMAIL");
        assertTrue(result);
    }

    @Test
    public void testIsNotificationAllowed_SmsSystem_False() {
        when(preferenceRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(preference));

        boolean result = preferenceService.isNotificationAllowed(userId, "SYSTEM", "SMS");
        assertFalse(result);
    }

    @Test
    public void testIsNotificationAllowed_EmailMarketing_False() {
        when(preferenceRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(preference));

        boolean result = preferenceService.isNotificationAllowed(userId, "MARKETING", "EMAIL");
        assertFalse(result);
    }

    @Test
    public void testGetPreferences_NotFound_CreatesDefault() {
        when(preferenceRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(Optional.empty());
        when(preferenceRepository.save(any(NotificationPreference.class))).thenAnswer(i -> i.getArguments()[0]);
        
        PreferenceDto.Response mockResponse = new PreferenceDto.Response(
                UUID.randomUUID(), userId, true, true, true, true, true, true, true
        );
        when(mapper.toResponse(any(NotificationPreference.class))).thenReturn(mockResponse);

        PreferenceDto.Response response = preferenceService.getPreferences(userId);
        assertNotNull(response);
        assertTrue(response.emailEnabled());
        verify(preferenceRepository, times(1)).save(any(NotificationPreference.class));
    }
}
