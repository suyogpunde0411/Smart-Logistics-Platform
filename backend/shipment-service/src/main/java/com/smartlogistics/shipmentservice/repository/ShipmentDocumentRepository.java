package com.smartlogistics.shipmentservice.repository;

import com.smartlogistics.shipmentservice.entity.ShipmentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentDocumentRepository extends JpaRepository<ShipmentDocument, UUID> {
    List<ShipmentDocument> findByShipmentIdAndIsDeletedFalse(UUID shipmentId);
    Optional<ShipmentDocument> findByIdAndIsDeletedFalse(UUID id);
}
