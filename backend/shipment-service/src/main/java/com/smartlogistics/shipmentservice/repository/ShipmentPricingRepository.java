package com.smartlogistics.shipmentservice.repository;

import com.smartlogistics.shipmentservice.entity.ShipmentPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentPricingRepository extends JpaRepository<ShipmentPricing, UUID> {
    Optional<ShipmentPricing> findByShipmentIdAndIsDeletedFalse(UUID shipmentId);
}
