package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.client.ShipmentServiceClient;
import com.smartlogistics.matchingservice.client.ShipmentServiceClient.DetailedShipmentResponse;
import com.smartlogistics.matchingservice.constants.MatchingConstants;
import com.smartlogistics.matchingservice.dto.MatchRequestDto;
import com.smartlogistics.matchingservice.dto.MatchResultDto;
import com.smartlogistics.matchingservice.entity.MatchRequest;
import com.smartlogistics.matchingservice.entity.MatchResult;
import com.smartlogistics.matchingservice.events.MatchingKafkaProducer;
import com.smartlogistics.matchingservice.events.ShipmentReadyForMatchingEvent;
import com.smartlogistics.matchingservice.mapper.MatchMapper;
import com.smartlogistics.matchingservice.repository.*;
import com.smartlogistics.shared.exception.ResourceNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

    @Mock private MatchRequestRepository matchRequestRepository;
    @Mock private MatchResultRepository matchResultRepository;
    @Mock private AcceptedMatchRepository acceptedMatchRepository;
    @Mock private RejectedMatchRepository rejectedMatchRepository;
    @Mock private MatchingAuditRepository auditRepository;
    @Mock private RecommendationEngine recommendationEngine;
    @Mock private RecommendationProvider recommendationProvider;
    @Mock private ShipmentServiceClient shipmentServiceClient;
    @Mock private MatchingKafkaProducer kafkaProducer;
    @Mock private MatchMapper mapper;

    @InjectMocks
    private MatchingServiceImpl matchingService;

    private UUID shipmentId;
    private DetailedShipmentResponse shipmentResponse;
    private MatchRequest matchRequest;

    @BeforeEach
    void setUp() {
        shipmentId = UUID.randomUUID();
        shipmentResponse = new DetailedShipmentResponse(
                shipmentId,
                "TRK54321",
                UUID.randomUUID(),
                "Origin", 12.9716, 77.5946,
                "Destination", 13.0827, 80.2707,
                "AVAILABLE",
                "BOX",
                500.0,
                2.0,
                "CONTAINER",
                10000.0,
                LocalDateTime.now().plusDays(1)
        );

        matchRequest = MatchRequest.builder()
                .shipmentId(shipmentId)
                .businessId(shipmentResponse.businessOwnerId())
                .status("PENDING")
                .expiresAt(LocalDateTime.now().plusHours(2))
                .build();
    }

    @Test
    void createMatchRequest_Success_RunsEngineAndPublishesEvents() {
        // Arrange
        MatchRequestDto.CreateRequest request = new MatchRequestDto.CreateRequest(shipmentId, 100.0, 150.0, 120);
        when(shipmentServiceClient.getShipmentById(shipmentId)).thenReturn(shipmentResponse);
        when(matchRequestRepository.save(any(MatchRequest.class))).thenReturn(matchRequest);

        MatchResult mockResult = MatchResult.builder()
                .shipmentId(shipmentId)
                .truckId(UUID.randomUUID())
                .overallScore(85.0)
                .estimatedCost(12000.0)
                .build();
        when(recommendationEngine.generateRecommendations(shipmentId)).thenReturn(List.of(mockResult));

        // Act
        matchingService.createMatchRequest(request);

        // Assert
        verify(matchRequestRepository, times(1)).save(any(MatchRequest.class));
        verify(recommendationEngine, times(1)).generateRecommendations(shipmentId);
        verify(kafkaProducer, times(1)).publishMatchCreated(any(), eq(shipmentId), any(), anyDouble());
        verify(auditRepository, times(1)).save(any());
    }

    @Test
    void runManualMatching_ShipmentNotFound_ThrowsException() {
        // Arrange
        when(recommendationEngine.generateRecommendations(shipmentId)).thenThrow(new ResourceNotFoundException("Shipment not found"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> matchingService.runManualMatching(shipmentId));
    }

    @Test
    void processAutomaticMatching_Success_GeneratesRecommendations() {
        // Arrange
        ShipmentReadyForMatchingEvent event = new ShipmentReadyForMatchingEvent(
                UUID.randomUUID(),
                LocalDateTime.now(),
                "corr-123",
                shipmentId,
                shipmentResponse.businessOwnerId(),
                "Origin", 12.9716, 77.5946,
                "Dest", 13.0827, 80.2707,
                500.0, 2.0, "BOX", "CONTAINER", 10000.0
        );

        MatchResult mockResult = MatchResult.builder()
                .shipmentId(shipmentId)
                .truckId(UUID.randomUUID())
                .overallScore(90.0)
                .estimatedCost(11000.0)
                .build();
        when(recommendationEngine.generateRecommendations(shipmentId)).thenReturn(List.of(mockResult));

        // Act
        matchingService.processAutomaticMatching(event);

        // Assert
        verify(matchRequestRepository, times(1)).save(any(MatchRequest.class));
        verify(recommendationEngine, times(1)).generateRecommendations(shipmentId);
        verify(kafkaProducer, times(1)).publishMatchCreated(any(), eq(shipmentId), any(), anyDouble());
    }
}
