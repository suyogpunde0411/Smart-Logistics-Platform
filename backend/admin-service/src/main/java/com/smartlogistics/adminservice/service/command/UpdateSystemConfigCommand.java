package com.smartlogistics.adminservice.service.command;

import com.smartlogistics.adminservice.entity.AdminAction;
import com.smartlogistics.adminservice.entity.SystemConfiguration;
import com.smartlogistics.adminservice.repository.AdminActionRepository;
import com.smartlogistics.adminservice.repository.SystemConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateSystemConfigCommand {

    private final SystemConfigurationRepository configRepository;
    private final AdminActionRepository adminActionRepository;

    @Transactional
    public SystemConfiguration execute(String key, String value, UUID adminId, String reason) {
        SystemConfiguration config = configRepository.findByConfigKeyAndIsDeletedFalse(key)
                .orElseGet(() -> SystemConfiguration.builder()
                        .configKey(key)
                        .module("GENERAL")
                        .build());

        config.setConfigValue(value);
        if (reason != null) {
            config.setDescription(reason);
        }
        SystemConfiguration saved = configRepository.save(config);

        adminActionRepository.save(AdminAction.builder()
                .adminId(adminId)
                .actionType("UPDATE_CONFIG")
                .targetEntityId(saved.getId())
                .targetEntityType("SYSTEM_CONFIGURATION")
                .details("Updated system config key: " + key + " to value: " + value + " Reason: " + reason)
                .build());

        return saved;
    }
}
