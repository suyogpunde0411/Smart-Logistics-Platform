package com.smartlogistics.truckservice.repository;

import com.smartlogistics.truckservice.entity.TruckLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TruckLocationRepository extends JpaRepository<TruckLocation, UUID> {
    Optional<TruckLocation> findByTruckIdAndIsDeletedFalse(UUID truckId);
}
