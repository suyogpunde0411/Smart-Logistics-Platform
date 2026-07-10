package com.smartlogistics.truckservice.repository;

import com.smartlogistics.truckservice.entity.TruckImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TruckImageRepository extends JpaRepository<TruckImage, UUID> {
    Optional<TruckImage> findByIdAndIsDeletedFalse(UUID id);
    List<TruckImage> findByTruckIdAndIsDeletedFalse(UUID truckId);
}
