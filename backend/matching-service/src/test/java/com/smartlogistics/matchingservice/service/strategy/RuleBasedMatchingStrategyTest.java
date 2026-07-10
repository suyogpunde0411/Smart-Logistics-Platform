package com.smartlogistics.matchingservice.service.strategy;

import com.smartlogistics.common.client.ReviewFeignClient;
import com.smartlogistics.matchingservice.client.ShipmentServiceClient.DetailedShipmentResponse;
import com.smartlogistics.matchingservice.constants.MatchingConstants;
import com.smartlogistics.matchingservice.dto.MatchResultDto;
import com.smartlogistics.matchingservice.entity.MatchRequest;
import com.smartlogistics.matchingservice.entity.MatchRule;
import com.smartlogistics.matchingservice.repository.MatchRuleRepository;
import com.smartlogistics.shared.dto.TruckDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.smartlogistics.common.client.TruckFeignClient;

@ExtendWith(MockitoExtension.class)
class RuleBasedMatchingStrategyTest {

    @Mock
    private MatchRuleRepository ruleRepository;

    @Mock
    private ReviewFeignClient reviewFeignClient;

    @Mock
    private TruckFeignClient truckFeignClient;

    @InjectMocks
    private RuleBasedMatchingStrategy matchingStrategy;

    private MatchRequest matchRequest;
    private DetailedShipmentResponse shipment;
    private TruckDTO.Response truck;

    @BeforeEach
    void setUp() {
        matchRequest = MatchRequest.builder()
                .shipmentId(UUID.randomUUID())
                .businessId(UUID.randomUUID())
                .status("PENDING")
                .radiusKm(100.0)
                .maxDistanceKm(100.0)
                .build();

        shipment = new DetailedShipmentResponse(
                matchRequest.getShipmentId(),
                "TRK12345",
                matchRequest.getBusinessId(),
                "Origin Address", 12.9716, 77.5946, // Bangalore
                "Dest Address", 13.0827, 80.2707, // Chennai
                "CREATED",
                "BOX",
                1000.0,
                5.0,
                "CONTAINER",
                15000.0,
                LocalDateTime.now().plusDays(2)
        );

        truck = new TruckDTO.Response(
                UUID.randomUUID(),
                "MH12AB1234",
                UUID.randomUUID(),
                "AVAILABLE",
                new TruckDTO.CapacityDto(1000.0, 5.0),
                new TruckDTO.AvailabilityDto("AVAILABLE", true),
                new TruckDTO.LocationDto(12.9716, 77.5946, 0.0, 0.0, 0.0, LocalDateTime.now()),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private List<MatchRule> mockRules() {
        return List.of(
                new MatchRule(MatchingConstants.RULE_PICKUP_DISTANCE, 30.0, true, "Distance"),
                new MatchRule(MatchingConstants.RULE_DESTINATION_SIMILARITY, 10.0, true, "Dest"),
                new MatchRule(MatchingConstants.RULE_TRUCK_CAPACITY, 15.0, true, "Capacity"),
                new MatchRule(MatchingConstants.RULE_CARGO_COMPATIBILITY, 10.0, true, "Cargo"),
                new MatchRule(MatchingConstants.RULE_AVAILABILITY, 10.0, true, "Avail"),
                new MatchRule(MatchingConstants.RULE_VEHICLE_TYPE, 10.0, true, "Type"),
                new MatchRule(MatchingConstants.RULE_DRIVER_RATING, 10.0, true, "Rating"),
                new MatchRule(MatchingConstants.RULE_BUSINESS_PREFERENCE, 5.0, true, "Pref")
        );
    }

    @Test
    void calculateScore_PerfectMatch_ReturnsHighOverallScore() {
        // Arrange
        when(ruleRepository.findByActiveTrueAndIsDeletedFalse()).thenReturn(mockRules());
        when(reviewFeignClient.getAverageRating(any(UUID.class))).thenReturn(5.0);
        when(truckFeignClient.getTruck(any(UUID.class))).thenReturn(new TruckFeignClient.InternalTruckResponse(
                UUID.randomUUID(), UUID.randomUUID(), "MH12AB1234", "CONTAINER", 2000.0, "AVAILABLE"
        ));

        // Truck at shipment origin, matches capacity, status AVAILABLE, type CONTAINER
        TruckDTO.CapacityDto capacity = new TruckDTO.CapacityDto(2000.0, 10.0);
        TruckDTO.LocationDto location = new TruckDTO.LocationDto(12.9716, 77.5946, 0.0, 0.0, 0.0, LocalDateTime.now());
        
        truck = new TruckDTO.Response(
                UUID.randomUUID(),
                "MH12AB1234",
                UUID.randomUUID(),
                "AVAILABLE",
                capacity,
                new TruckDTO.AvailabilityDto("AVAILABLE", true),
                location,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        MatchResultDto.ScoreDetails score = matchingStrategy.calculateScore(matchRequest, shipment, truck);

        // Assert
        assertNotNull(score);
        assertTrue(score.overallScore() > 80.0, "Overall score should be high for perfect match");
        assertEquals(100.0, score.distanceScore());
        assertEquals(100.0, score.availabilityScore());
    }

    @Test
    void calculateScore_ConstraintViolation_ReturnsZeroScore() {
        // Arrange
        when(ruleRepository.findByActiveTrueAndIsDeletedFalse()).thenReturn(mockRules());
        when(truckFeignClient.getTruck(any(UUID.class))).thenReturn(new TruckFeignClient.InternalTruckResponse(
                UUID.randomUUID(), UUID.randomUUID(), "MH12AB1234", "CONTAINER", 2000.0, "MAINTENANCE"
        ));

        // Truck at shipment origin, matches capacity, status MAINTENANCE (not AVAILABLE)
        TruckDTO.CapacityDto capacity = new TruckDTO.CapacityDto(2000.0, 10.0);
        TruckDTO.LocationDto location = new TruckDTO.LocationDto(12.9716, 77.5946, 0.0, 0.0, 0.0, LocalDateTime.now());
        
        truck = new TruckDTO.Response(
                UUID.randomUUID(),
                "MH12AB1234",
                UUID.randomUUID(),
                "MAINTENANCE",
                capacity,
                new TruckDTO.AvailabilityDto("MAINTENANCE", false),
                location,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        MatchResultDto.ScoreDetails score = matchingStrategy.calculateScore(matchRequest, shipment, truck);

        // Assert
        assertNotNull(score);
        assertEquals(0.0, score.overallScore(), "Overall score should be zero due to hard constraint violation (status)");
    }
}
