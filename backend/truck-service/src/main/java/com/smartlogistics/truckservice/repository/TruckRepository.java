package com.smartlogistics.truckservice.repository;

import com.smartlogistics.truckservice.entity.Truck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TruckRepository extends JpaRepository<Truck, UUID> {

    Optional<Truck> findByIdAndIsDeletedFalse(UUID id);

    Optional<Truck> findByRegistrationNumberAndIsDeletedFalse(String registrationNumber);

    boolean existsByRegistrationNumberAndIsDeletedFalse(String registrationNumber);

    @Query("SELECT DISTINCT t FROM Truck t " +
            "LEFT JOIN t.capacity c " +
            "LEFT JOIN t.availability a " +
            "LEFT JOIN t.location l " +
            "WHERE t.isDeleted = false " +
            "AND (:regNum IS NULL OR LOWER(t.registrationNumber) LIKE LOWER(CONCAT('%', :regNum, '%'))) " +
            "AND (:ownerId IS NULL OR t.ownerId = :ownerId) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:availStatus IS NULL OR a.status = :availStatus) " +
            "AND (:active IS NULL OR a.active = :active) " +
            "AND (:minWeight IS NULL OR c.maxWeight >= :minWeight) " +
            "AND (:minVolume IS NULL OR c.maxVolume >= :minVolume) ")
    Page<Truck> searchTrucks(
            @Param("regNum") String regNum,
            @Param("ownerId") UUID ownerId,
            @Param("status") String status,
            @Param("availStatus") String availStatus,
            @Param("active") Boolean active,
            @Param("minWeight") Double minWeight,
            @Param("minVolume") Double minVolume,
            Pageable pageable
    );

    @Query(value = "SELECT t.* FROM trucks t " +
            "INNER JOIN truck_locations l ON t.id = l.truck_id " +
            "INNER JOIN truck_availabilities a ON t.id = a.truck_id " +
            "WHERE t.is_deleted = false AND a.active = true AND a.status = 'AVAILABLE' " +
            "AND (6371 * acos(cos(radians(:lat)) * cos(radians(l.latitude)) * cos(radians(l.longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(l.latitude)))) <= :radius",
            nativeQuery = true)
    Page<Truck> findNearbyAvailableTrucks(
            @Param("lat") Double lat,
            @Param("lng") Double lng,
            @Param("radius") Double radius,
            Pageable pageable
    );
}
