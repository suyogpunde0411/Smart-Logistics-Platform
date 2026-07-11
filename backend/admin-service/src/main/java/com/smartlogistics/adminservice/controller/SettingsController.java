package com.smartlogistics.adminservice.controller;

import com.smartlogistics.adminservice.dto.FeatureFlagDto;
import com.smartlogistics.adminservice.dto.SystemConfigurationDto;
import com.smartlogistics.adminservice.service.SystemSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/settings")
@Tag(name = "Settings", description = "System Settings APIs")
public class SettingsController {

    private final SystemSettingsService settingsService;

    public SettingsController(SystemSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("/configurations")
    @Operation(summary = "Get All System Configurations")
    public ResponseEntity<List<SystemConfigurationDto>> getAllConfigurations() {
        return ResponseEntity.ok(settingsService.getAllConfigurations());
    }

    @PutMapping("/configurations/{key}")
    @Operation(summary = "Update System Configuration")
    public ResponseEntity<Void> updateConfiguration(
            @PathVariable String key,
            @RequestParam String value,
            @RequestHeader("X-User-Id") UUID adminId,
            @RequestParam String reason) {
        settingsService.updateSystemConfiguration(key, value, adminId, reason);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feature-flags")
    @Operation(summary = "Get All Feature Flags")
    public ResponseEntity<List<FeatureFlagDto>> getAllFeatureFlags() {
        return ResponseEntity.ok(settingsService.getAllFeatureFlags());
    }
    
    @PostMapping("/feature-flags")
    @Operation(summary = "Create Feature Flag")
    public ResponseEntity<FeatureFlagDto> createFeatureFlag(@RequestBody FeatureFlagDto dto) {
        return ResponseEntity.ok(settingsService.createFeatureFlag(dto));
    }

    @PutMapping("/feature-flags/{flagKey}")
    @Operation(summary = "Toggle Feature Flag")
    public ResponseEntity<Void> toggleFeatureFlag(
            @PathVariable String flagKey,
            @RequestParam boolean enable,
            @RequestHeader("X-User-Id") UUID adminId,
            @RequestParam String reason) {
        settingsService.toggleFeatureFlag(flagKey, enable, adminId, reason);
        return ResponseEntity.ok().build();
    }
}
