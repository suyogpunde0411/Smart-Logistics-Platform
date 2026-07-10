package com.smartlogistics.truckservice.repository;

import com.smartlogistics.truckservice.entity.TruckInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TruckInsuranceRepository extends JpaRepository<TruckInsurance, UUID> {
    Optional<TruckInsurance> findByTruckIdAndIsDeletedFalse(UUID truckId);
}
