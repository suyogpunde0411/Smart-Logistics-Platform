package com.smartlogistics.truckservice.repository;

import com.smartlogistics.truckservice.entity.TruckStatusHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TruckStatusHistoryRepository extends JpaRepository<TruckStatusHistory, UUID> {
    Page<TruckStatusHistory> findByTruckIdAndIsDeletedFalseOrderByChangedAtDesc(UUID truckId, Pageable pageable);
}
