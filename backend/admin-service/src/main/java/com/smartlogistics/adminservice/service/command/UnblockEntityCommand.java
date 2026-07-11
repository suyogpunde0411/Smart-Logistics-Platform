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
public class UnblockEntityCommand {

    private final BlockedEntityRepository blockedEntityRepository;
    private final AdminActionRepository adminActionRepository;

    @Transactional
    public BlockedEntity execute(UUID entityId, UUID adminId, String reason) {
        BlockedEntity block = blockedEntityRepository.findByBlockedEntityIdAndIsDeletedFalse(entityId)
                .orElseThrow(() -> new IllegalArgumentException("Blocked entity not found"));

        block.setDeleted(true);
        BlockedEntity savedBlock = blockedEntityRepository.save(block);

        adminActionRepository.save(AdminAction.builder()
                .adminId(adminId)
                .actionType("UNBLOCK_ENTITY")
                .targetEntityId(entityId)
                .targetEntityType(block.getEntityType())
                .details("Unblocked entity with reason: " + reason)
                .build());

        return savedBlock;
    }
}
