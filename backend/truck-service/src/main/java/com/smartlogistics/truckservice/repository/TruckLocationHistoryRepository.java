package com.smartlogistics.truckservice.repository;

import com.smartlogistics.truckservice.entity.TruckLocationHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TruckLocationHistoryRepository extends JpaRepository<TruckLocationHistory, UUID> {
    Page<TruckLocationHistory> findByTruckIdAndIsDeletedFalseOrderByTimestampDesc(UUID truckId, Pageable pageable);
}
