package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.DailyMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DailyMetricsRepository extends JpaRepository<DailyMetrics, UUID> {
    Optional<DailyMetrics> findByDateAndIsDeletedFalse(LocalDate date);
    List<DailyMetrics> findByDateBetweenAndIsDeletedFalseOrderByDateAsc(LocalDate startDate, LocalDate endDate);
}
