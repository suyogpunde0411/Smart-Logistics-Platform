package com.smartlogistics.adminservice.service.command;

import com.smartlogistics.adminservice.entity.AdminAction;
import com.smartlogistics.adminservice.entity.ModerationRecord;
import com.smartlogistics.adminservice.repository.AdminActionRepository;
import com.smartlogistics.adminservice.repository.ModerationRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModerateReviewCommand {

    private final ModerationRecordRepository moderationRecordRepository;
    private final AdminActionRepository adminActionRepository;

    @Transactional
    public ModerationRecord execute(UUID reviewId, UUID adminId, String reason) {
        ModerationRecord record = ModerationRecord.builder()
                .reportedEntityId(reviewId)
                .entityType("REVIEW")
                .actionTaken("REMOVE")
                .reason(reason)
                .moderatedBy(adminId)
                .build();
        
        ModerationRecord savedRecord = moderationRecordRepository.save(record);

        adminActionRepository.save(AdminAction.builder()
                .adminId(adminId)
                .actionType("MODERATE_REVIEW")
                .targetEntityId(savedRecord.getId())
                .targetEntityType("MODERATION_RECORD")
                .details("Removed review " + reviewId + " with reason: " + reason)
                .build());

        return savedRecord;
    }
}
