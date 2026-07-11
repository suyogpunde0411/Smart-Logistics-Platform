package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.YearlyMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface YearlyMetricsRepository extends JpaRepository<YearlyMetrics, UUID> {
    Optional<YearlyMetrics> findByYearAndIsDeletedFalse(Integer year);
    List<YearlyMetrics> findByIsDeletedFalseOrderByYearAsc();
}
