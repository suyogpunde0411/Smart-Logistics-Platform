package com.smartlogistics.shipmentservice.repository;

import com.smartlogistics.shipmentservice.entity.ShipmentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentImageRepository extends JpaRepository<ShipmentImage, UUID> {
    List<ShipmentImage> findByShipmentIdAndIsDeletedFalse(UUID shipmentId);
    Optional<ShipmentImage> findByIdAndIsDeletedFalse(UUID id);
}
