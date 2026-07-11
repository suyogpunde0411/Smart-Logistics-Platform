package com.smartlogistics.reviewservice.repository;

import com.smartlogistics.reviewservice.entity.ReputationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReputationHistoryRepository extends JpaRepository<ReputationHistory, UUID> {
    List<ReputationHistory> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(UUID userId);
}
