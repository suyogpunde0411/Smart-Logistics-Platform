package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.ReviewAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ReviewAnalyticsRepository extends JpaRepository<ReviewAnalytics, UUID> {
    Optional<ReviewAnalytics> findByRevieweeIdAndIsDeletedFalse(UUID revieweeId);
}
