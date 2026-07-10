package com.smartlogistics.shipmentservice.service;

import com.smartlogistics.common.client.UserFeignClient;
import com.smartlogistics.shared.enums.ShipmentStatus;
import com.smartlogistics.shared.util.StorageService;
import com.smartlogistics.shipmentservice.dto.ShipmentDto;
import com.smartlogistics.shipmentservice.entity.Shipment;
import com.smartlogistics.shipmentservice.entity.ShipmentCategory;
import com.smartlogistics.shipmentservice.entity.ShipmentStatusHistory;
import com.smartlogistics.shipmentservice.events.ShipmentEventPublisher;
import com.smartlogistics.shipmentservice.exception.*;
import com.smartlogistics.shipmentservice.mapper.ShipmentMapper;
import com.smartlogistics.shipmentservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShipmentServiceTest {

    @Mock private ShipmentRepository shipmentRepository;
    @Mock private ShipmentItemRepository itemRepository;
    @Mock private ShipmentDocumentRepository documentRepository;
    @Mock private ShipmentImageRepository imageRepository;
    @Mock private PickupDetailsRepository pickupDetailsRepository;
    @Mock private DropDetailsRepository dropDetailsRepository;
    @Mock private ShipmentPricingRepository pricingRepository;
    @Mock private ShipmentStatusHistoryRepository statusHistoryRepository;
    @Mock private ShipmentCategoryRepository categoryRepository;

    @Mock private ShipmentMapper shipmentMapper;
    @Mock private ShipmentEventPublisher eventPublisher;
    @Mock private UserFeignClient userFeignClient;
    @Mock private StorageService storageService;

    @InjectMocks
    private ShipmentService shipmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                UUID.randomUUID().toString(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        ));
    }

    @Test
    public void testCreateShipment_Success() {
        UUID businessOwnerId = UUID.randomUUID();

        ShipmentDto.CreateRequest request = new ShipmentDto.CreateRequest(
                businessOwnerId,
                "Mumbai, MH", 19.0760, 72.8777,
                "Delhi, DL", 28.7041, 77.1025,
                "GENERAL_CARGO", 500.0, null, "KG", "CBM",
                "Test shipment", 10000.0, "INR",
                "TRAILER", null,
                null, null, null, null, null
        );

        UserFeignClient.InternalUserResponse mockUser = new UserFeignClient.InternalUserResponse(
                businessOwnerId, "test@example.com", "9876543210", "John", "Doe", "ACTIVE");
        when(userFeignClient.getUser(businessOwnerId)).thenReturn(mockUser);
        when(categoryRepository.findByCodeAndIsDeletedFalse("GENERAL_CARGO"))
                .thenReturn(Optional.of(activeCategory("GENERAL_CARGO")));

        Shipment savedShipment = Shipment.builder()
                .trackingNumber("SHP123")
                .businessOwnerId(businessOwnerId)
                .originAddress("Mumbai, MH")
                .destinationAddress("Delhi, DL")
                .cargoType("GENERAL_CARGO")
                .totalWeight(500.0)
                .status(ShipmentStatus.CREATED)
                .build();
        savedShipment.setId(UUID.randomUUID());

        when(shipmentRepository.save(any(Shipment.class))).thenReturn(savedShipment);

        ShipmentDto.Response mockResponse = new ShipmentDto.Response(
                savedShipment.getId(), "SHP123", businessOwnerId,
                "Mumbai, MH", 19.0760, 72.8777,
                "Delhi, DL", 28.7041, 77.1025,
                "CREATED", "GENERAL_CARGO", 500.0, null, "KG", "CBM",
                null, 10000.0, "INR", "TRAILER", null,
                null, null, null, null, null, null, null,
                null, null
        );
        when(shipmentMapper.toResponse(any(Shipment.class))).thenReturn(mockResponse);

        ShipmentDto.Response result = shipmentService.createShipment(request);

        assertNotNull(result);
        assertEquals("SHP123", result.trackingNumber());
        assertEquals("CREATED", result.status());
        verify(eventPublisher, times(1)).publishShipmentCreated(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testCreateShipment_InvalidWeight_ThrowsException() {
        UUID businessOwnerId = UUID.randomUUID();

        UserFeignClient.InternalUserResponse mockUser = new UserFeignClient.InternalUserResponse(
                businessOwnerId, "test@example.com", "9876543210", "John", "Doe", "ACTIVE");
        when(userFeignClient.getUser(businessOwnerId)).thenReturn(mockUser);
        when(categoryRepository.findByCodeAndIsDeletedFalse("GENERAL_CARGO"))
                .thenReturn(Optional.of(activeCategory("GENERAL_CARGO")));

        ShipmentDto.CreateRequest request = new ShipmentDto.CreateRequest(
                businessOwnerId,
                "Mumbai, MH", 19.0760, 72.8777,
                "Delhi, DL", 28.7041, 77.1025,
                "GENERAL_CARGO", -1.0, null, "KG", "CBM",
                null, null, "INR", null, null,
                null, null, null, null, null
        );

        assertThrows(InvalidWeightException.class, () -> shipmentService.createShipment(request));
    }

    @Test
    public void testCreateShipment_InvalidCategory_ThrowsException() {
        UUID businessOwnerId = UUID.randomUUID();

        UserFeignClient.InternalUserResponse mockUser = new UserFeignClient.InternalUserResponse(
                businessOwnerId, "test@example.com", "9876543210", "John", "Doe", "ACTIVE");
        when(userFeignClient.getUser(businessOwnerId)).thenReturn(mockUser);
        when(categoryRepository.findByCodeAndIsDeletedFalse("UNKNOWN")).thenReturn(Optional.empty());

        ShipmentDto.CreateRequest request = new ShipmentDto.CreateRequest(
                businessOwnerId,
                "Mumbai, MH", 19.0760, 72.8777,
                "Delhi, DL", 28.7041, 77.1025,
                "UNKNOWN", 100.0, null, "KG", "CBM",
                null, null, "INR", null, null,
                null, null, null, null, null
        );

        assertThrows(InvalidShipmentStateException.class, () -> shipmentService.createShipment(request));
    }

    @Test
    public void testGetShipment_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(shipmentRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());
        assertThrows(ShipmentNotFoundException.class, () -> shipmentService.getShipment(id));
    }

    @Test
    public void testCancelShipment_ValidTransition() {
        UUID id = UUID.randomUUID();
        UUID businessOwnerId = UUID.randomUUID();

        Shipment shipment = Shipment.builder()
                .businessOwnerId(businessOwnerId)
                .originAddress("Mumbai")
                .destinationAddress("Delhi")
                .cargoType("GENERAL")
                .totalWeight(100.0)
                .status(ShipmentStatus.CREATED)
                .build();
        shipment.setId(id);

        when(shipmentRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(shipment));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);

        ShipmentDto.Response mockResponse = new ShipmentDto.Response(
                id, "SHP123", businessOwnerId, "Mumbai", 0.0, 0.0,
                "Delhi", 0.0, 0.0, "CANCELLED", "GENERAL", 100.0,
                null, "KG", "CBM", null, null, "INR", null, null,
                null, null, null, null, null, null, null, null, null
        );
        when(shipmentMapper.toResponse(any(Shipment.class))).thenReturn(mockResponse);

        ShipmentDto.Response result = shipmentService.cancelShipment(id, "No longer needed");

        assertNotNull(result);
        assertEquals("CANCELLED", result.status());
        verify(eventPublisher).publishShipmentCancelled(eq(id), eq(businessOwnerId), eq("No longer needed"));
    }

    @Test
    public void testUpdateStatus_InvalidTransition_ThrowsException() {
        UUID id = UUID.randomUUID();

        Shipment shipment = Shipment.builder()
                .businessOwnerId(UUID.randomUUID())
                .originAddress("Mumbai")
                .destinationAddress("Delhi")
                .cargoType("GENERAL")
                .totalWeight(100.0)
                .status(ShipmentStatus.DELIVERED)
                .build();
        shipment.setId(id);

        when(shipmentRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(shipment));

        ShipmentDto.StatusUpdateRequest request = new ShipmentDto.StatusUpdateRequest(ShipmentStatus.CREATED, null);
        assertThrows(InvalidShipmentStateException.class, () -> shipmentService.updateStatus(id, request));
    }

    @Test
    public void testUpdatePricing_NegativeAmount_ThrowsException() {
        UUID id = UUID.randomUUID();
        Shipment shipment = Shipment.builder()
                .businessOwnerId(UUID.randomUUID())
                .originAddress("Mumbai")
                .destinationAddress("Delhi")
                .cargoType("GENERAL_CARGO")
                .totalWeight(100.0)
                .status(ShipmentStatus.CREATED)
                .build();
        shipment.setId(id);

        when(shipmentRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(shipment));

        ShipmentDto.PricingUpdateRequest request =
                new ShipmentDto.PricingUpdateRequest(null, null, null, null, null, -1.0, "INR", "ESTIMATED");

        assertThrows(InvalidShipmentStateException.class, () -> shipmentService.updatePricing(id, request));
    }

    @Test
    public void testUpdateShipment_NonOwner_ThrowsAccessDenied() {
        UUID ownerId = UUID.randomUUID();
        UUID differentUserId = UUID.randomUUID();
        UUID shipmentId = UUID.randomUUID();

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                differentUserId.toString(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_BUSINESS_OWNER"))
        ));

        Shipment shipment = Shipment.builder()
                .businessOwnerId(ownerId)
                .originAddress("Mumbai")
                .destinationAddress("Delhi")
                .cargoType("GENERAL_CARGO")
                .totalWeight(100.0)
                .status(ShipmentStatus.CREATED)
                .build();
        shipment.setId(shipmentId);

        when(shipmentRepository.findByIdAndIsDeletedFalse(shipmentId)).thenReturn(Optional.of(shipment));

        ShipmentDto.UpdateRequest request = new ShipmentDto.UpdateRequest(
                "Pune", null, null, null, null, null,
                null, null, null, null, null, null, null, null
        );

        assertThrows(AccessDeniedException.class, () -> shipmentService.updateShipment(shipmentId, request));
    }

    @Test
    public void testExpireOverdueShipments_PublishesExpiredEvent() {
        UUID shipmentId = UUID.randomUUID();
        UUID businessOwnerId = UUID.randomUUID();
        Shipment shipment = Shipment.builder()
                .businessOwnerId(businessOwnerId)
                .originAddress("Mumbai")
                .destinationAddress("Delhi")
                .cargoType("GENERAL_CARGO")
                .totalWeight(100.0)
                .status(ShipmentStatus.CREATED)
                .expiresAt(LocalDateTime.now().minusHours(1))
                .build();
        shipment.setId(shipmentId);

        when(shipmentRepository.findExpiredShipments(any(LocalDateTime.class))).thenReturn(List.of(shipment));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);
        when(statusHistoryRepository.save(any(ShipmentStatusHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        shipmentService.expireOverdueShipments();

        assertEquals(ShipmentStatus.EXPIRED, shipment.getStatus());
        verify(statusHistoryRepository).save(any(ShipmentStatusHistory.class));
        verify(eventPublisher).publishShipmentExpired(shipmentId, businessOwnerId);
    }

    private ShipmentCategory activeCategory(String code) {
        return ShipmentCategory.builder()
                .code(code)
                .displayName(code)
                .active(true)
                .build();
    }
}
