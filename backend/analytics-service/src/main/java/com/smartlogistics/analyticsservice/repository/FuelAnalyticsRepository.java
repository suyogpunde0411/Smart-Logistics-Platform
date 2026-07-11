package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.FuelAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface FuelAnalyticsRepository extends JpaRepository<FuelAnalytics, UUID> {
    Optional<FuelAnalytics> findByTruckTypeAndRouteKeyAndIsDeletedFalse(String truckType, String routeKey);
}
