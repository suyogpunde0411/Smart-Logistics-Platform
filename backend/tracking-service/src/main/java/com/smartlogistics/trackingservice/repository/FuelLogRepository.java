package com.smartlogistics.trackingservice.repository;

import com.smartlogistics.trackingservice.entity.FuelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FuelLogRepository extends JpaRepository<FuelLog, UUID> {
    List<FuelLog> findByTripIdAndIsDeletedFalseOrderByRefueledAtDesc(UUID tripId);
}
