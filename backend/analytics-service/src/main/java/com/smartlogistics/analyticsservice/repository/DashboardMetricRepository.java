package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.DashboardMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface DashboardMetricRepository extends JpaRepository<DashboardMetric, UUID> {
    Optional<DashboardMetric> findByMetricKeyAndIsDeletedFalse(String metricKey);
}
