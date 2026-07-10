package com.smartlogistics.trackingservice.repository;

import com.smartlogistics.trackingservice.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<Trip, UUID> {

    Optional<Trip> findByIdAndIsDeletedFalse(UUID id);

    boolean existsByShipmentIdAndStatusNotAndIsDeletedFalse(UUID shipmentId, String status);

    List<Trip> findByStatusInAndIsDeletedFalse(List<String> statuses);

    @Query("SELECT t FROM Trip t WHERE " +
            "(:tripId IS NULL OR t.id = :tripId) AND " +
            "(:shipmentId IS NULL OR t.shipmentId = :shipmentId) AND " +
            "(:truckId IS NULL OR t.truckId = :truckId) AND " +
            "(:driverId IS NULL OR t.driverId = :driverId) AND " +
            "(:businessId IS NULL OR t.businessId = :businessId) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:startDate IS NULL OR t.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR t.createdAt <= :endDate)")
    Page<Trip> searchTrips(
            @Param("tripId") UUID tripId,
            @Param("shipmentId") UUID shipmentId,
            @Param("truckId") UUID truckId,
            @Param("driverId") UUID driverId,
            @Param("businessId") UUID businessId,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
