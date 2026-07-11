package com.smartlogistics.adminservice.repository;

import com.smartlogistics.adminservice.entity.ModerationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ModerationRecordRepository extends JpaRepository<ModerationRecord, UUID> {
    List<ModerationRecord> findByReportedEntityIdAndIsDeletedFalse(UUID reportedEntityId);
}
