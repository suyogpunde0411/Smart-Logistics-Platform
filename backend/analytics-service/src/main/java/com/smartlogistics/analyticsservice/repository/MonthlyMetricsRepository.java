package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.MonthlyMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MonthlyMetricsRepository extends JpaRepository<MonthlyMetrics, UUID> {
    Optional<MonthlyMetrics> findByMonthAndYearAndIsDeletedFalse(Integer month, Integer year);
    List<MonthlyMetrics> findByYearAndIsDeletedFalseOrderByMonthAsc(Integer year);
}
