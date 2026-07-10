package com.smartlogistics.truckservice.repository;

import com.smartlogistics.truckservice.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TruckRepositoryTest {

    @Autowired
    private TruckRepository truckRepository;

    @Test
    public void testSaveAndSearchTrucks() {
        Truck truck = Truck.builder()
                .registrationNumber("MH12AB1234")
                .ownerId(UUID.randomUUID())
                .status("ACTIVE")
                .build();

        TruckCapacity capacity = TruckCapacity.builder()
                .truck(truck)
                .maxWeight(1000.0)
                .maxVolume(10.0)
                .build();
        truck.setCapacity(capacity);

        TruckAvailability availability = TruckAvailability.builder()
                .truck(truck)
                .status("AVAILABLE")
                .active(true)
                .build();
        truck.setAvailability(availability);

        TruckLocation location = TruckLocation.builder()
                .truck(truck)
                .latitude(18.5204)
                .longitude(73.8567)
                .timestamp(LocalDateTime.now())
                .build();
        truck.setLocation(location);

        Truck saved = truckRepository.save(truck);
        assertNotNull(saved.getId());

        Page<Truck> searchResults = truckRepository.searchTrucks(
                "MH12", saved.getOwnerId(), "ACTIVE", "AVAILABLE", true, 500.0, 5.0, PageRequest.of(0, 10));

        assertEquals(1, searchResults.getTotalElements());
        assertEquals("MH12AB1234", searchResults.getContent().get(0).getRegistrationNumber());
    }

    @Test
    public void testFindNearbyAvailableTrucks() {
        UUID ownerId = UUID.randomUUID();
        
        Truck truck1 = Truck.builder().registrationNumber("MH12AB1111").ownerId(ownerId).status("ACTIVE").build();
        truck1.setCapacity(TruckCapacity.builder().truck(truck1).maxWeight(1000.0).maxVolume(10.0).build());
        truck1.setAvailability(TruckAvailability.builder().truck(truck1).status("AVAILABLE").active(true).build());
        truck1.setLocation(TruckLocation.builder().truck(truck1).latitude(18.5204).longitude(73.8567).timestamp(LocalDateTime.now()).build());

        Truck truck2 = Truck.builder().registrationNumber("MH12AB2222").ownerId(ownerId).status("ACTIVE").build();
        truck2.setCapacity(TruckCapacity.builder().truck(truck2).maxWeight(1000.0).maxVolume(10.0).build());
        truck2.setAvailability(TruckAvailability.builder().truck(truck2).status("AVAILABLE").active(true).build());
        truck2.setLocation(TruckLocation.builder().truck(truck2).latitude(18.9750).longitude(72.8258).timestamp(LocalDateTime.now()).build());

        truckRepository.save(truck1);
        truckRepository.save(truck2);

        Page<Truck> nearby = truckRepository.findNearbyAvailableTrucks(18.5204, 73.8567, 50.0, PageRequest.of(0, 10));
        assertEquals(1, nearby.getTotalElements());
        assertEquals("MH12AB1111", nearby.getContent().get(0).getRegistrationNumber());
    }
}
