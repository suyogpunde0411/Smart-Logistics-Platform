package com.smartlogistics.adminservice.service;

import com.smartlogistics.adminservice.entity.ModerationRecord;
import com.smartlogistics.adminservice.entity.BlockedEntity;
import com.smartlogistics.adminservice.repository.ModerationRecordRepository;
import com.smartlogistics.adminservice.repository.BlockedEntityRepository;
import com.smartlogistics.adminservice.service.command.ModerateReviewCommand;
import com.smartlogistics.adminservice.service.command.SuspendUserCommand;
import com.smartlogistics.adminservice.service.command.UnblockEntityCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.List;

@Service
@Transactional
public class ModerationService {

    private final ModerateReviewCommand moderateReviewCommand;
    private final SuspendUserCommand suspendUserCommand;
    private final UnblockEntityCommand unblockEntityCommand;
    private final ModerationRecordRepository moderationRecordRepository;
    private final BlockedEntityRepository blockedEntityRepository;

    public ModerationService(ModerateReviewCommand moderateReviewCommand,
                             SuspendUserCommand suspendUserCommand,
                             UnblockEntityCommand unblockEntityCommand,
                             ModerationRecordRepository moderationRecordRepository,
                             BlockedEntityRepository blockedEntityRepository) {
        this.moderateReviewCommand = moderateReviewCommand;
        this.suspendUserCommand = suspendUserCommand;
        this.unblockEntityCommand = unblockEntityCommand;
        this.moderationRecordRepository = moderationRecordRepository;
        this.blockedEntityRepository = blockedEntityRepository;
    }

    public void removeReview(UUID reviewId, UUID adminId, String reason) {
        moderateReviewCommand.execute(reviewId, adminId, reason);
    }

    public void suspendUser(UUID userId, UUID adminId, String reason) {
        suspendUserCommand.execute(userId, adminId, reason);
    }

    public void unblockEntity(UUID entityId, UUID adminId, String reason) {
        unblockEntityCommand.execute(entityId, adminId, reason);
    }
    
    public List<ModerationRecord> getAllModerationRecords() {
        return moderationRecordRepository.findAll();
    }
    
    public List<BlockedEntity> getAllBlockedEntities() {
        return blockedEntityRepository.findAll();
    }
}
