package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.CarbonSavings;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CarbonSavingsRepository extends JpaRepository<CarbonSavings, UUID> {
    Optional<CarbonSavings> findByEntityIdAndEntityTypeAndIsDeletedFalse(UUID entityId, String entityType);
}
