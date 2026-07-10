package com.smartlogistics.truckservice.repository;

import com.smartlogistics.truckservice.entity.TruckAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TruckAvailabilityRepository extends JpaRepository<TruckAvailability, UUID> {}
