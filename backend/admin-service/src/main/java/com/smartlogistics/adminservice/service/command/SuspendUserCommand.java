package com.smartlogistics.adminservice.service.command;

import com.smartlogistics.adminservice.entity.AdminAction;
import com.smartlogistics.adminservice.entity.BlockedEntity;
import com.smartlogistics.adminservice.repository.AdminActionRepository;
import com.smartlogistics.adminservice.repository.BlockedEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuspendUserCommand {

    private final BlockedEntityRepository blockedEntityRepository;
    private final AdminActionRepository adminActionRepository;

    @Transactional
    public BlockedEntity execute(UUID userId, UUID adminId, String reason) {
        BlockedEntity block = blockedEntityRepository.findByBlockedEntityIdAndIsDeletedFalse(userId)
                .orElseGet(() -> BlockedEntity.builder()
                        .blockedEntityId(userId)
                        .entityType("USER")
                        .build());

        block.setReason(reason);
        block.setBlockedBy(adminId);
        BlockedEntity savedBlock = blockedEntityRepository.save(block);

        adminActionRepository.save(AdminAction.builder()
                .adminId(adminId)
                .actionType("SUSPEND_USER")
                .targetEntityId(userId)
                .targetEntityType("USER")
                .details("Suspended user with reason: " + reason)
                .build());

        return savedBlock;
    }
}
