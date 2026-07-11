package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.DriverAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DriverAnalyticsRepository extends JpaRepository<DriverAnalytics, UUID> {
    Optional<DriverAnalytics> findByDriverIdAndIsDeletedFalse(UUID driverId);

    @Query("SELECT d FROM DriverAnalytics d WHERE d.isDeleted = false ORDER BY d.completedTrips DESC")
    List<DriverAnalytics> findTopDrivers();
}
