package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.RevenueAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RevenueAnalyticsRepository extends JpaRepository<RevenueAnalytics, UUID> {
    Optional<RevenueAnalytics> findByDateAndIsDeletedFalse(LocalDate date);
    List<RevenueAnalytics> findByDateBetweenAndIsDeletedFalseOrderByDateAsc(LocalDate startDate, LocalDate endDate);
}
