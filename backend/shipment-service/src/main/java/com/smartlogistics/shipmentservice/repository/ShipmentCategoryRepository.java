package com.smartlogistics.shipmentservice.repository;

import com.smartlogistics.shipmentservice.entity.ShipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentCategoryRepository extends JpaRepository<ShipmentCategory, UUID> {
    Optional<ShipmentCategory> findByCodeAndIsDeletedFalse(String code);
    List<ShipmentCategory> findByActiveTrueAndIsDeletedFalse();
    boolean existsByCodeAndIsDeletedFalse(String code);
}
