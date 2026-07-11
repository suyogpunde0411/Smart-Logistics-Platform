package com.smartlogistics.adminservice.service.command;

import com.smartlogistics.adminservice.entity.AdminAction;
import com.smartlogistics.adminservice.entity.FeatureFlag;
import com.smartlogistics.adminservice.repository.AdminActionRepository;
import com.smartlogistics.adminservice.repository.FeatureFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToggleFeatureFlagCommand {

    private final FeatureFlagRepository flagRepository;
    private final AdminActionRepository adminActionRepository;

    @Transactional
    public FeatureFlag execute(String flagKey, String isEnabled, UUID adminId, String reason) {
        FeatureFlag flag = flagRepository.findByFlagNameAndIsDeletedFalse(flagKey)
                .orElseGet(() -> FeatureFlag.builder()
                        .flagName(flagKey)
                        .createdBy(adminId)
                        .build());

        flag.setIsEnabled(Boolean.parseBoolean(isEnabled));
        if (reason != null) {
            flag.setDescription(reason);
        }
        FeatureFlag saved = flagRepository.save(flag);

        adminActionRepository.save(AdminAction.builder()
                .adminId(adminId)
                .actionType("TOGGLE_FEATURE_FLAG")
                .targetEntityId(saved.getId())
                .targetEntityType("FEATURE_FLAG")
                .details("Toggled feature flag: " + flagKey + " to " + isEnabled + ". Reason: " + reason)
                .build());

        return saved;
    }
}
