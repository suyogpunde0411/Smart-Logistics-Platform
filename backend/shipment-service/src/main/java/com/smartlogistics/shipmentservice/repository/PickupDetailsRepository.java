package com.smartlogistics.shipmentservice.repository;

import com.smartlogistics.shipmentservice.entity.PickupDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PickupDetailsRepository extends JpaRepository<PickupDetails, UUID> {
    Optional<PickupDetails> findByShipmentIdAndIsDeletedFalse(UUID shipmentId);
}
