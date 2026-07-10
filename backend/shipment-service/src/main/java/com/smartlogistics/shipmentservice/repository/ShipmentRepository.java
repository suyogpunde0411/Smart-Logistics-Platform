package com.smartlogistics.shipmentservice.repository;

import com.smartlogistics.shared.enums.ShipmentStatus;
import com.smartlogistics.shipmentservice.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

    Optional<Shipment> findByIdAndIsDeletedFalse(UUID id);

    Optional<Shipment> findByTrackingNumberAndIsDeletedFalse(String trackingNumber);

    boolean existsByTrackingNumberAndIsDeletedFalse(String trackingNumber);

    @Query("SELECT s FROM Shipment s " +
            "WHERE s.isDeleted = false " +
            "AND (:businessOwnerId IS NULL OR s.businessOwnerId = :businessOwnerId) " +
            "AND (:status IS NULL OR s.status = :status) " +
            "AND (:cargoType IS NULL OR s.cargoType = :cargoType) " +
            "AND (:minWeight IS NULL OR s.totalWeight >= :minWeight) " +
            "AND (:maxWeight IS NULL OR s.totalWeight <= :maxWeight) " +
            "AND (:requiredTruckType IS NULL OR s.requiredTruckType = :requiredTruckType) " +
            "AND (:originCity IS NULL OR LOWER(s.originAddress) LIKE LOWER(CONCAT('%', :originCity, '%'))) " +
            "AND (:destinationCity IS NULL OR LOWER(s.destinationAddress) LIKE LOWER(CONCAT('%', :destinationCity, '%'))) " +
            "AND (:pickupAfter IS NULL OR EXISTS (SELECT p FROM PickupDetails p WHERE p.shipment = s AND p.scheduledAt >= :pickupAfter)) " +
            "AND (:deliveryBefore IS NULL OR EXISTS (SELECT d FROM DropDetails d WHERE d.shipment = s AND d.scheduledAt <= :deliveryBefore))")
    Page<Shipment> searchShipments(
            @Param("businessOwnerId") UUID businessOwnerId,
            @Param("status") ShipmentStatus status,
            @Param("cargoType") String cargoType,
            @Param("minWeight") Double minWeight,
            @Param("maxWeight") Double maxWeight,
            @Param("requiredTruckType") String requiredTruckType,
            @Param("originCity") String originCity,
            @Param("destinationCity") String destinationCity,
            @Param("pickupAfter") LocalDateTime pickupAfter,
            @Param("deliveryBefore") LocalDateTime deliveryBefore,
            Pageable pageable
    );

    @Query(value = "SELECT s.* FROM shipments s " +
            "INNER JOIN pickup_details p ON s.id = p.shipment_id " +
            "WHERE s.is_deleted = false AND s.status IN ('CREATED', 'AVAILABLE') " +
            "AND (6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(p.latitude)))) <= :radius",
            nativeQuery = true)
    Page<Shipment> findShipmentsNearOrigin(
            @Param("lat") Double lat,
            @Param("lng") Double lng,
            @Param("radius") Double radius,
            Pageable pageable
    );

    @Query("SELECT s FROM Shipment s WHERE s.isDeleted = false AND s.status = 'CREATED' AND s.expiresAt <= :now")
    java.util.List<Shipment> findExpiredShipments(@Param("now") LocalDateTime now);
}
