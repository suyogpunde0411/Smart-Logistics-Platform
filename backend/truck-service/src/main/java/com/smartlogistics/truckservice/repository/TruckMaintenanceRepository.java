package com.smartlogistics.truckservice.repository;

import com.smartlogistics.truckservice.entity.TruckMaintenance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TruckMaintenanceRepository extends JpaRepository<TruckMaintenance, UUID> {
    Optional<TruckMaintenance> findByIdAndIsDeletedFalse(UUID id);
    Page<TruckMaintenance> findByTruckIdAndIsDeletedFalse(UUID truckId, Pageable pageable);
}
