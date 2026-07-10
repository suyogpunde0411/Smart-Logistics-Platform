package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.client.ShipmentServiceClient;
import com.smartlogistics.matchingservice.client.ShipmentServiceClient.DetailedShipmentResponse;
import com.smartlogistics.matchingservice.constants.MatchingConstants;
import com.smartlogistics.matchingservice.dto.BidDto;
import com.smartlogistics.matchingservice.entity.*;
import com.smartlogistics.matchingservice.events.MatchingKafkaProducer;
import com.smartlogistics.matchingservice.exception.DuplicateBidException;
import com.smartlogistics.matchingservice.exception.InvalidMatchException;
import com.smartlogistics.matchingservice.exception.ShipmentUnavailableException;
import com.smartlogistics.matchingservice.mapper.MatchMapper;
import com.smartlogistics.matchingservice.repository.*;
import com.smartlogistics.shared.security.CurrentUserUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @Mock private BidRepository bidRepository;
    @Mock private MatchResultRepository matchResultRepository;
    @Mock private MatchRequestRepository matchRequestRepository;
    @Mock private AcceptedMatchRepository acceptedMatchRepository;
    @Mock private RejectedMatchRepository rejectedMatchRepository;
    @Mock private BidHistoryRepository bidHistoryRepository;
    @Mock private MatchingAuditRepository auditRepository;
    @Mock private ShipmentServiceClient shipmentServiceClient;
    @Mock private MatchingKafkaProducer kafkaProducer;
    @Mock private MatchMapper mapper;

    @InjectMocks
    private BidServiceImpl bidService;

    private UUID driverId;
    private UUID matchResultId;
    private UUID shipmentId;
    private MatchResult matchResult;
    private DetailedShipmentResponse shipmentResponse;

    @BeforeEach
    void setUp() {
        driverId = UUID.randomUUID();
        CurrentUserUtil.setUserId(driverId);

        matchResultId = UUID.randomUUID();
        shipmentId = UUID.randomUUID();

        MatchRequest req = MatchRequest.builder()
                .businessId(UUID.randomUUID())
                .expiresAt(LocalDateTime.now().plusHours(2))
                .build();
        req.setId(UUID.randomUUID());

        matchResult = MatchResult.builder()
                .shipmentId(shipmentId)
                .truckId(UUID.randomUUID())
                .driverId(driverId)
                .matchRequest(req)
                .status("RECOMMENDED")
                .overallScore(80.0)
                .build();
        matchResult.setId(matchResultId);

        shipmentResponse = new DetailedShipmentResponse(
                shipmentId,
                "TRK99887",
                req.getBusinessId(),
                "Origin", 12.9716, 77.5946,
                "Dest", 13.0827, 80.2707,
                "AVAILABLE",
                "BOX",
                500.0,
                2.0,
                "CONTAINER",
                12000.0,
                LocalDateTime.now().plusDays(2)
        );
    }

    @AfterEach
    void tearDown() {
        CurrentUserUtil.clear();
    }

    @Test
    void placeBid_Success_SavesBidAndPublishesPlacedEvent() {
        // Arrange
        BidDto.CreateRequest request = new BidDto.CreateRequest(matchResultId, 11000.0, "INR", "Ready", 60);
        when(matchResultRepository.findByIdAndIsDeletedFalse(matchResultId)).thenReturn(Optional.of(matchResult));
        when(shipmentServiceClient.getShipmentById(shipmentId)).thenReturn(shipmentResponse);
        when(bidRepository.existsByMatchResultIdAndDriverIdAndIsDeletedFalse(matchResultId, driverId)).thenReturn(false);

        Bid mockSavedBid = Bid.builder()
                .matchResult(matchResult)
                .shipmentId(shipmentId)
                .truckId(matchResult.getTruckId())
                .driverId(driverId)
                .amount(11000.0)
                .currency("INR")
                .status("PENDING")
                .build();
        mockSavedBid.setId(UUID.randomUUID());
        when(bidRepository.save(any(Bid.class))).thenReturn(mockSavedBid);

        // Act
        bidService.placeBid(request);

        // Assert
        verify(bidRepository, times(1)).save(any(Bid.class));
        verify(kafkaProducer, times(1)).publishBidPlaced(any(), eq(shipmentId), eq(driverId), eq(11000.0));
        verify(auditRepository, times(1)).save(any());
        verify(bidHistoryRepository, times(1)).save(any());
    }

    @Test
    void placeBid_DuplicateBid_ThrowsException() {
        // Arrange
        BidDto.CreateRequest request = new BidDto.CreateRequest(matchResultId, 11000.0, "INR", "Ready", 60);
        when(matchResultRepository.findByIdAndIsDeletedFalse(matchResultId)).thenReturn(Optional.of(matchResult));
        when(shipmentServiceClient.getShipmentById(shipmentId)).thenReturn(shipmentResponse);
        when(bidRepository.existsByMatchResultIdAndDriverIdAndIsDeletedFalse(matchResultId, driverId)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateBidException.class, () -> bidService.placeBid(request));
    }

    @Test
    void acceptBid_Success_TransitionsStatusesAndPublishesEvents() {
        // Arrange
        UUID bidId = UUID.randomUUID();
        Bid pendingBid = Bid.builder()
                .matchResult(matchResult)
                .shipmentId(shipmentId)
                .truckId(matchResult.getTruckId())
                .driverId(driverId)
                .businessId(matchResult.getMatchRequest().getBusinessId())
                .amount(11500.0)
                .currency("INR")
                .status("PENDING")
                .build();
        pendingBid.setId(bidId);

        when(bidRepository.findByIdAndIsDeletedFalse(bidId)).thenReturn(Optional.of(pendingBid));
        when(shipmentServiceClient.getShipmentById(shipmentId)).thenReturn(shipmentResponse);
        when(bidRepository.save(any(Bid.class))).thenReturn(pendingBid);

        // Act
        bidService.acceptBid(bidId);

        // Assert
        assertEquals(MatchingConstants.BID_STATUS_ACCEPTED, pendingBid.getStatus());
        assertEquals(MatchingConstants.MATCH_STATUS_ACCEPTED, matchResult.getStatus());
        assertEquals(MatchingConstants.REQ_STATUS_COMPLETED, matchResult.getMatchRequest().getStatus());

        verify(acceptedMatchRepository, times(1)).save(any(AcceptedMatch.class));
        verify(kafkaProducer, times(1)).publishBidAccepted(eq(matchResult.getId()), eq(shipmentId), eq(driverId), eq(matchResult.getTruckId()), eq(11500.0));
    }

    @Test
    void acceptBid_ShipmentUnavailable_ThrowsException() {
        // Arrange
        UUID bidId = UUID.randomUUID();
        Bid pendingBid = Bid.builder()
                .matchResult(matchResult)
                .shipmentId(shipmentId)
                .status("PENDING")
                .build();
        pendingBid.setId(bidId);

        shipmentResponse = new DetailedShipmentResponse(
                shipmentId, "TRK99887", UUID.randomUUID(), "Origin", 12.9, 77.5, "Dest", 13.0, 80.2,
                "MATCHED", "BOX", 500.0, 2.0, "CONTAINER", 12000.0, LocalDateTime.now().plusDays(2) // status MATCHED
        );

        when(bidRepository.findByIdAndIsDeletedFalse(bidId)).thenReturn(Optional.of(pendingBid));
        when(shipmentServiceClient.getShipmentById(shipmentId)).thenReturn(shipmentResponse);

        // Act & Assert
        assertThrows(ShipmentUnavailableException.class, () -> bidService.acceptBid(bidId));
    }
}
