package com.smartlogistics.matchingservice.repository;

import com.smartlogistics.matchingservice.entity.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {
    Optional<Bid> findByIdAndIsDeletedFalse(UUID id);
    boolean existsByMatchResultIdAndDriverIdAndIsDeletedFalse(UUID matchResultId, UUID driverId);
    Page<Bid> findByShipmentIdAndIsDeletedFalse(UUID shipmentId, Pageable pageable);
    List<Bid> findByStatusAndExpiresAtBeforeAndIsDeletedFalse(String status, LocalDateTime now);
}
