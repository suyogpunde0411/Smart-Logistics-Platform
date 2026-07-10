package com.smartlogistics.notificationservice.controller;

import com.smartlogistics.notificationservice.dto.PreferenceDto;
import com.smartlogistics.notificationservice.service.NotificationPreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications/preferences")
@RequiredArgsConstructor
@Tag(name = "Notification Preferences", description = "Endpoints for checking and updating user alerts configurations")
public class PreferenceController {

    private final NotificationPreferenceService preferenceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get user notification preferences settings")
    public ResponseEntity<PreferenceDto.Response> getPreferences() {
        UUID userId = UUID.fromString(getLoggedUserId());
        return ResponseEntity.ok(preferenceService.getPreferences(userId));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Update user notification preference settings")
    public ResponseEntity<PreferenceDto.Response> updatePreferences(@RequestBody PreferenceDto.UpdateRequest request) {
        UUID userId = UUID.fromString(getLoggedUserId());
        return ResponseEntity.ok(preferenceService.updatePreferences(userId, request));
    }

    private String getLoggedUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
