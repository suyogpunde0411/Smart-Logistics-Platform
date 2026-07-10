package com.smartlogistics.trackingservice.service;

import com.smartlogistics.common.event.MatchAcceptedEvent;
import com.smartlogistics.trackingservice.client.ShipmentClient;
import com.smartlogistics.trackingservice.client.TruckClient;
import com.smartlogistics.trackingservice.client.UserClient;
import com.smartlogistics.trackingservice.dto.TripDto;
import com.smartlogistics.trackingservice.dto.TripRouteDto;
import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.entity.TripRoute;
import com.smartlogistics.trackingservice.events.TrackingKafkaProducer;
import com.smartlogistics.trackingservice.mapper.TripMapper;
import com.smartlogistics.trackingservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

    @Mock private TripRepository tripRepository;
    @Mock private TripRouteRepository routeRepository;
    @Mock private TripCheckpointRepository checkpointRepository;
    @Mock private TripTimelineRepository timelineRepository;
    @Mock private TripSummaryRepository summaryRepository;
    @Mock private ShipmentClient shipmentClient;
    @Mock private TruckClient truckClient;
    @Mock private UserClient userClient;
    @Mock private TrackingKafkaProducer kafkaProducer;
    @Mock private TripMapper tripMapper;

    @InjectMocks
    private TripServiceImpl tripService;

    private UUID tripId;
    private UUID shipmentId;
    private UUID truckId;
    private UUID driverId;
    private UUID businessId;
    private Trip trip;

    @BeforeEach
    public void setUp() {
        tripId = UUID.randomUUID();
        shipmentId = UUID.randomUUID();
        truckId = UUID.randomUUID();
        driverId = UUID.randomUUID();
        businessId = UUID.randomUUID();

        trip = Trip.builder()
                .shipmentId(shipmentId)
                .truckId(truckId)
                .driverId(driverId)
                .businessId(businessId)
                .status("ASSIGNED")
                .totalDistanceTravelledKm(0.0)
                .remainingDistanceKm(500.0)
                .averageSpeedKmh(50.0)
                .build();
        trip.setId(tripId);
    }

    @Test
    public void testCreateTripFromMatch_Success() {
        when(tripRepository.existsByShipmentIdAndStatusNotAndIsDeletedFalse(any(), anyString()))
                .thenReturn(false);

        ShipmentClient.InternalShipmentResponse shipmentMock = new ShipmentClient.InternalShipmentResponse(
                shipmentId, businessId, "Mumbai", "Delhi", 100.0, 500.0, "CREATED"
        );
        when(shipmentClient.getShipment(shipmentId)).thenReturn(shipmentMock);

        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> {
            Trip t = invocation.getArgument(0);
            t.setId(tripId);
            return t;
        });

        MatchAcceptedEvent event = new MatchAcceptedEvent(
                "eventId", Instant.now(), "correlationId", UUID.randomUUID(), shipmentId, driverId, truckId, 1000.0
        );

        tripService.createTripFromMatch(event);

        verify(tripRepository).save(any(Trip.class));
        verify(kafkaProducer).publishTripCreated(eq(tripId), eq(shipmentId), eq(truckId), eq(driverId), eq(businessId));
    }

    @Test
    public void testStartTrip_Success() {
        trip.setStatus("READY");
        trip.setRoute(TripRoute.builder()
                .startLatitude(19.076)
                .startLongitude(72.877)
                .endLatitude(28.613)
                .endLongitude(77.209)
                .startAddress("Mumbai")
                .endAddress("Delhi")
                .build());

        when(tripRepository.findByIdAndIsDeletedFalse(tripId)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        tripService.startTrip(tripId);

        assertEquals("STARTED", trip.getStatus());
        assertNotNull(trip.getStartedAt());
        verify(kafkaProducer).publishTripStarted(eq(tripId), eq(shipmentId), eq(driverId), eq(truckId));
    }

    @Test
    public void testPauseTrip_Success() {
        trip.setStatus("STARTED");
        when(tripRepository.findByIdAndIsDeletedFalse(tripId)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        tripService.pauseTrip(tripId);

        assertEquals("PAUSED", trip.getStatus());
        verify(kafkaProducer).publishTripPaused(eq(tripId));
    }

    @Test
    public void testCancelTrip_Success() {
        when(tripRepository.findByIdAndIsDeletedFalse(tripId)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        tripService.cancelTrip(tripId, "Cancelled by user");

        assertEquals("CANCELLED", trip.getStatus());
        verify(kafkaProducer).publishTripCancelled(eq(tripId), eq("Cancelled by user"));
    }
}
