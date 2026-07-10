package com.smartlogistics.shipmentservice.repository;

import com.smartlogistics.shipmentservice.entity.ShipmentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentItemRepository extends JpaRepository<ShipmentItem, UUID> {
    List<ShipmentItem> findByShipmentIdAndIsDeletedFalse(UUID shipmentId);
    Optional<ShipmentItem> findByIdAndIsDeletedFalse(UUID id);
}
