package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.ShipmentAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ShipmentAnalyticsRepository extends JpaRepository<ShipmentAnalytics, UUID> {
    Optional<ShipmentAnalytics> findByShipmentIdAndIsDeletedFalse(UUID shipmentId);
}
