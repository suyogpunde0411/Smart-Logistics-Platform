package com.smartlogistics.shipmentservice.repository;

import com.smartlogistics.shipmentservice.entity.DropDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DropDetailsRepository extends JpaRepository<DropDetails, UUID> {
    Optional<DropDetails> findByShipmentIdAndIsDeletedFalse(UUID shipmentId);
}
