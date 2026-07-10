package com.smartlogistics.truckservice.repository;

import com.smartlogistics.truckservice.entity.TruckDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TruckDocumentRepository extends JpaRepository<TruckDocument, UUID> {
    Optional<TruckDocument> findByIdAndIsDeletedFalse(UUID id);
    List<TruckDocument> findByTruckIdAndIsDeletedFalse(UUID truckId);
}
