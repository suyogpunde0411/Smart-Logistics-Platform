package com.smartlogistics.matchingservice.repository;

import com.smartlogistics.matchingservice.entity.MatchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, UUID> {
    Optional<MatchResult> findByIdAndIsDeletedFalse(UUID id);
    boolean existsByShipmentIdAndTruckIdAndIsDeletedFalse(UUID shipmentId, UUID truckId);
    List<MatchResult> findTop10ByShipmentIdAndIsDeletedFalseOrderByOverallScoreDesc(UUID shipmentId);
    List<MatchResult> findTop10ByTruckIdAndIsDeletedFalseOrderByOverallScoreDesc(UUID truckId);
    List<MatchResult> findByStatusAndExpiresAtBeforeAndIsDeletedFalse(String status, LocalDateTime now);

    @Query("SELECT m FROM MatchResult m WHERE m.isDeleted = false " +
            "AND (:shipmentId IS NULL OR m.shipmentId = :shipmentId) " +
            "AND (:truckId IS NULL OR m.truckId = :truckId) " +
            "AND (:driverId IS NULL OR m.driverId = :driverId) " +
            "AND (:status IS NULL OR m.status = :status) " +
            "AND (:minScore IS NULL OR m.overallScore >= :minScore) " +
            "AND (:maxScore IS NULL OR m.overallScore <= :maxScore)")
    Page<MatchResult> search(@Param("shipmentId") UUID shipmentId,
                             @Param("truckId") UUID truckId,
                             @Param("driverId") UUID driverId,
                             @Param("status") String status,
                             @Param("minScore") Double minScore,
                             @Param("maxScore") Double maxScore,
                             Pageable pageable);
}
