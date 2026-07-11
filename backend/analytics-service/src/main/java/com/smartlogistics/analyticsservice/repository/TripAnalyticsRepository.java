package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.TripAnalytics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripAnalyticsRepository extends JpaRepository<TripAnalytics, UUID> {
    
    Optional<TripAnalytics> findByTripIdAndIsDeletedFalse(UUID tripId);

    List<TripAnalytics> findByDriverIdAndIsDeletedFalse(UUID driverId);

    @Query("SELECT t FROM TripAnalytics t LEFT JOIN ShipmentAnalytics s ON t.shipmentId = s.shipmentId WHERE " +
           "(:driverId IS NULL OR t.driverId = :driverId) AND " +
           "(:businessId IS NULL OR t.businessId = :businessId) AND " +
           "(:truckId IS NULL OR t.truckId = :truckId) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:city IS NULL OR LOWER(s.originAddress) LIKE LOWER(CONCAT('%', :city, '%')) OR LOWER(s.destinationAddress) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:startDate IS NULL OR t.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR t.createdAt <= :endDate) AND " +
           "t.isDeleted = false")
    Page<TripAnalytics> searchTrips(
            @Param("driverId") UUID driverId,
            @Param("businessId") UUID businessId,
            @Param("truckId") UUID truckId,
            @Param("status") String status,
            @Param("city") String city,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT s.originAddress as routeFrom, s.destinationAddress as routeTo, COUNT(t.tripId) as tripCount " +
           "FROM TripAnalytics t JOIN ShipmentAnalytics s ON t.shipmentId = s.shipmentId " +
           "WHERE t.isDeleted = false " +
           "GROUP BY s.originAddress, s.destinationAddress " +
           "ORDER BY COUNT(t.tripId) DESC")
    List<Object[]> findTopRoutes();
}
