package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.WeeklyMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeeklyMetricsRepository extends JpaRepository<WeeklyMetrics, UUID> {
    Optional<WeeklyMetrics> findByWeekStartDateAndIsDeletedFalse(LocalDate date);
    List<WeeklyMetrics> findByWeekStartDateBetweenAndIsDeletedFalseOrderByWeekStartDateAsc(LocalDate start, LocalDate end);
}
