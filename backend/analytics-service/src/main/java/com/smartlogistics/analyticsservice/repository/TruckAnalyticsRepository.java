package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.TruckAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TruckAnalyticsRepository extends JpaRepository<TruckAnalytics, UUID> {
    Optional<TruckAnalytics> findByTruckIdAndIsDeletedFalse(UUID truckId);

    @Query("SELECT t FROM TruckAnalytics t WHERE t.isDeleted = false ORDER BY t.totalDistanceKm DESC")
    List<TruckAnalytics> findTopTrucksByDistance();
}
