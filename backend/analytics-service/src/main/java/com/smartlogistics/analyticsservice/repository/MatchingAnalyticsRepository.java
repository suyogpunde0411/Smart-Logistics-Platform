package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.MatchingAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface MatchingAnalyticsRepository extends JpaRepository<MatchingAnalytics, UUID> {
    
    @Query("SELECT m FROM MatchingAnalytics m WHERE m.isDeleted = false")
    Optional<MatchingAnalytics> findLatestAndIsDeletedFalse();
}
