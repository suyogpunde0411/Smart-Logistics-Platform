package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.BusinessAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessAnalyticsRepository extends JpaRepository<BusinessAnalytics, UUID> {
    Optional<BusinessAnalytics> findByBusinessIdAndIsDeletedFalse(UUID businessId);

    @Query("SELECT b FROM BusinessAnalytics b WHERE b.isDeleted = false ORDER BY b.totalBudgetSpent DESC")
    List<BusinessAnalytics> findTopBusinesses();
}
