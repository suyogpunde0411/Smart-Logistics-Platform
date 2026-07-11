package com.smartlogistics.adminservice.service;

import com.smartlogistics.adminservice.dto.SystemConfigurationDto;
import com.smartlogistics.adminservice.dto.FeatureFlagDto;
import com.smartlogistics.adminservice.entity.SystemConfiguration;
import com.smartlogistics.adminservice.entity.FeatureFlag;
import com.smartlogistics.adminservice.repository.SystemConfigurationRepository;
import com.smartlogistics.adminservice.repository.FeatureFlagRepository;
import com.smartlogistics.adminservice.service.command.UpdateSystemConfigCommand;
import com.smartlogistics.adminservice.service.command.ToggleFeatureFlagCommand;
import com.smartlogistics.adminservice.mapper.AdminMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SystemSettingsService {

    private final UpdateSystemConfigCommand updateConfigCommand;
    private final ToggleFeatureFlagCommand toggleFlagCommand;
    private final SystemConfigurationRepository configRepository;
    private final FeatureFlagRepository flagRepository;
    private final AdminMapper mapper;

    public SystemSettingsService(UpdateSystemConfigCommand updateConfigCommand,
                                 ToggleFeatureFlagCommand toggleFlagCommand,
                                 SystemConfigurationRepository configRepository,
                                 FeatureFlagRepository flagRepository,
                                 AdminMapper mapper) {
        this.updateConfigCommand = updateConfigCommand;
        this.toggleFlagCommand = toggleFlagCommand;
        this.configRepository = configRepository;
        this.flagRepository = flagRepository;
        this.mapper = mapper;
    }

    public void updateSystemConfiguration(String key, String value, UUID adminId, String reason) {
        updateConfigCommand.execute(key, value, adminId, reason);
    }

    public void toggleFeatureFlag(String flagKey, boolean enable, UUID adminId, String reason) {
        toggleFlagCommand.execute(flagKey, String.valueOf(enable), adminId, reason);
    }

    public List<SystemConfigurationDto> getAllConfigurations() {
        return configRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FeatureFlagDto> getAllFeatureFlags() {
        return flagRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    public FeatureFlagDto createFeatureFlag(FeatureFlagDto dto) {
        FeatureFlag flag = mapper.toEntity(dto);
        return mapper.toDto(flagRepository.save(flag));
    }
}
