package com.smartlogistics.truckservice.service;

import com.smartlogistics.common.client.UserFeignClient;
import com.smartlogistics.shared.dto.TruckDTO;
import com.smartlogistics.truckservice.entity.*;
import com.smartlogistics.truckservice.events.TruckEventPublisher;
import com.smartlogistics.truckservice.exception.DuplicateRegistrationNumberException;
import com.smartlogistics.truckservice.mapper.TruckMapper;
import com.smartlogistics.truckservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TruckServiceTest {

    @Mock private TruckRepository truckRepository;
    @Mock private TruckCapacityRepository capacityRepository;
    @Mock private TruckAvailabilityRepository availabilityRepository;
    @Mock private TruckLocationRepository locationRepository;
    @Mock private TruckLocationHistoryRepository locationHistoryRepository;
    @Mock private TruckDocumentRepository documentRepository;
    @Mock private TruckInsuranceRepository insuranceRepository;
    @Mock private TruckMaintenanceRepository maintenanceRepository;
    @Mock private TruckImageRepository imageRepository;
    @Mock private TruckStatusHistoryRepository statusHistoryRepository;

    @Mock private TruckMapper truckMapper;
    @Mock private TruckEventPublisher eventPublisher;
    @Mock private UserFeignClient userFeignClient;

    @InjectMocks
    private TruckService truckService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterTruckSuccess() {
        UUID ownerId = UUID.randomUUID();
        TruckDTO.RegisterRequest request = new TruckDTO.RegisterRequest("MH12AB1234", ownerId, 1000.0, 10.0);

        when(truckRepository.existsByRegistrationNumberAndIsDeletedFalse(request.registrationNumber())).thenReturn(false);

        UserFeignClient.InternalUserResponse feignUser = new UserFeignClient.InternalUserResponse(
                ownerId, "test@test.com", "9876543210", "First", "Last", "ACTIVE");
        when(userFeignClient.getUser(ownerId)).thenReturn(feignUser);

        Truck savedTruck = Truck.builder()
                .registrationNumber(request.registrationNumber())
                .ownerId(ownerId)
                .status("ACTIVE")
                .build();
        savedTruck.setId(UUID.randomUUID());

        when(truckRepository.save(any(Truck.class))).thenReturn(savedTruck);

        TruckDTO.Response mockResponse = new TruckDTO.Response(
                savedTruck.getId(),
                "MH12AB1234",
                ownerId,
                "ACTIVE",
                new TruckDTO.CapacityDto(1000.0, 10.0),
                new TruckDTO.AvailabilityDto("AVAILABLE", true),
                new TruckDTO.LocationDto(0.0, 0.0, null, null, null, null),
                null,
                null,
                null
        );
        when(truckMapper.toResponse(any(Truck.class))).thenReturn(mockResponse);

        TruckDTO.Response result = truckService.registerTruck(request);

        assertNotNull(result);
        assertEquals("MH12AB1234", result.registrationNumber());
        verify(eventPublisher, times(1)).publishTruckRegistered(any(), any(), any(), any(), any());
    }

    @Test
    public void testRegisterTruckDuplicateThrowsException() {
        UUID ownerId = UUID.randomUUID();
        TruckDTO.RegisterRequest request = new TruckDTO.RegisterRequest("MH12AB1234", ownerId, 1000.0, 10.0);

        when(truckRepository.existsByRegistrationNumberAndIsDeletedFalse(request.registrationNumber())).thenReturn(true);

        assertThrows(DuplicateRegistrationNumberException.class, () -> truckService.registerTruck(request));
    }
}
