package com.smartlogistics.shipmentservice.service;

import com.smartlogistics.common.client.UserFeignClient;
import com.smartlogistics.shared.enums.ShipmentStatus;
import com.smartlogistics.shared.util.StorageService;
import com.smartlogistics.shipmentservice.dto.ShipmentDto;
import com.smartlogistics.shipmentservice.entity.*;
import com.smartlogistics.shipmentservice.events.ShipmentEventPublisher;
import com.smartlogistics.shipmentservice.exception.*;
import com.smartlogistics.shipmentservice.mapper.ShipmentMapper;
import com.smartlogistics.shipmentservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentItemRepository itemRepository;
    private final ShipmentDocumentRepository documentRepository;
    private final ShipmentImageRepository imageRepository;
    private final PickupDetailsRepository pickupDetailsRepository;
    private final DropDetailsRepository dropDetailsRepository;
    private final ShipmentPricingRepository pricingRepository;
    private final ShipmentStatusHistoryRepository statusHistoryRepository;
    private final ShipmentCategoryRepository categoryRepository;

    private final ShipmentMapper shipmentMapper;
    private final ShipmentEventPublisher eventPublisher;
    private final UserFeignClient userFeignClient;
    private final StorageService storageService;

    private static final String DOCUMENT_BUCKET = "shipment-documents";
    private static final String IMAGE_BUCKET = "shipment-images";
    private static final Set<String> ALLOWED_DOCUMENT_TYPES = Set.of(
            "INVOICE",
            "E_WAY_BILL",
            "DELIVERY_CHALLAN",
            "GST_INVOICE",
            "INSURANCE_COPY",
            "PURCHASE_ORDER",
            "OTHER"
    );

    // ─── State Machine ────────────────────────────────────────────────────────
    private static final java.util.Map<ShipmentStatus, Set<ShipmentStatus>> VALID_TRANSITIONS =
            java.util.Map.of(
                    ShipmentStatus.CREATED, Set.of(ShipmentStatus.AVAILABLE, ShipmentStatus.CANCELLED, ShipmentStatus.EXPIRED),
                    ShipmentStatus.AVAILABLE, Set.of(ShipmentStatus.MATCHED, ShipmentStatus.CANCELLED, ShipmentStatus.EXPIRED),
                    ShipmentStatus.MATCHED, Set.of(ShipmentStatus.IN_TRANSIT, ShipmentStatus.CANCELLED),
                    ShipmentStatus.IN_TRANSIT, Set.of(ShipmentStatus.DELIVERED),
                    ShipmentStatus.DELIVERED, Set.of(ShipmentStatus.COMPLETED)
            );

    // ─── Creation ─────────────────────────────────────────────────────────────

    @Transactional
    public ShipmentDto.Response createShipment(ShipmentDto.CreateRequest request) {
        log.info("Creating shipment for businessOwnerId: {}", request.businessOwnerId());

        assertOwnerOrAdmin(request.businessOwnerId());
        validateBusinessOwner(request.businessOwnerId());
        validateCategory(request.cargoType());
        validateWeight(request.totalWeight());

        if (request.pickupDetails() != null && request.dropDetails() != null) {
            validateSchedule(request.pickupDetails().scheduledAt(), request.dropDetails().scheduledAt());
        }

        String trackingNumber = generateTrackingNumber();

        Shipment shipment = Shipment.builder()
                .trackingNumber(trackingNumber)
                .businessOwnerId(request.businessOwnerId())
                .originAddress(request.originAddress())
                .originLatitude(request.originLatitude())
                .originLongitude(request.originLongitude())
                .destinationAddress(request.destinationAddress())
                .destinationLatitude(request.destinationLatitude())
                .destinationLongitude(request.destinationLongitude())
                .cargoType(request.cargoType())
                .totalWeight(request.totalWeight())
                .totalVolume(request.totalVolume())
                .description(request.description())
                .budgetAmount(request.budgetAmount())
                .budgetCurrency(request.budgetCurrency() != null ? request.budgetCurrency() : "INR")
                .weightUnit(request.weightUnit() != null ? request.weightUnit() : "KG")
                .volumeUnit(request.volumeUnit() != null ? request.volumeUnit() : "CBM")
                .requiredTruckType(request.requiredTruckType())
                .expiresAt(request.expiresAt())
                .status(ShipmentStatus.CREATED)
                .build();

        Shipment saved = shipmentRepository.save(shipment);

        // Pickup details
        if (request.pickupDetails() != null) {
            createPickupDetails(saved, request.pickupDetails());
        }

        // Drop details
        if (request.dropDetails() != null) {
            createDropDetails(saved, request.dropDetails());
        }

        // Dimension
        if (request.dimension() != null) {
            createDimension(saved, request.dimension());
        }

        // Items
        if (request.items() != null) {
            request.items().forEach(itemReq -> createItem(saved, itemReq));
        }

        // Requirements
        if (request.requirementTypes() != null) {
            request.requirementTypes().forEach(reqType -> {
                ShipmentRequirement req = ShipmentRequirement.builder()
                        .shipment(saved).requirementType(reqType).mandatory(true).build();
                saved.getRequirements().add(req);
            });
            shipmentRepository.save(saved);
        }

        eventPublisher.publishShipmentCreated(
                saved.getId(), saved.getBusinessOwnerId(),
                saved.getOriginAddress(), saved.getDestinationAddress(),
                saved.getTotalWeight(), saved.getCargoType(), saved.getBudgetAmount());

        log.info("Shipment created successfully with tracking: {}", trackingNumber);
        return shipmentMapper.toResponse(saved);
    }

    // ─── Read ─────────────────────────────────────────────────────────────────

    public ShipmentDto.Response getShipment(UUID id) {
        Shipment shipment = findShipmentById(id);
        return shipmentMapper.toResponse(shipment);
    }

    public ShipmentDto.Response getShipmentByTracking(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumberAndIsDeletedFalse(trackingNumber)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with tracking: " + trackingNumber));
        return shipmentMapper.toResponse(shipment);
    }

    public Page<ShipmentDto.Response> searchShipments(
            UUID businessOwnerId, ShipmentStatus status, String cargoType,
            Double minWeight, Double maxWeight, String requiredTruckType,
            String originCity, String destinationCity,
            LocalDateTime pickupAfter, LocalDateTime deliveryBefore,
            Pageable pageable) {
        return shipmentRepository.searchShipments(
                businessOwnerId, status, cargoType, minWeight, maxWeight,
                requiredTruckType, originCity, destinationCity, pickupAfter, deliveryBefore, pageable)
                .map(shipmentMapper::toResponse);
    }

    public Page<ShipmentDto.Response> findShipmentsNearOrigin(Double lat, Double lng, Double radius, Pageable pageable) {
        return shipmentRepository.findShipmentsNearOrigin(lat, lng, radius, pageable)
                .map(shipmentMapper::toResponse);
    }

    // ─── Update ───────────────────────────────────────────────────────────────

    @Transactional
    public ShipmentDto.Response updateShipment(UUID id, ShipmentDto.UpdateRequest request) {
        log.info("Updating shipment: {}", id);
        Shipment shipment = findShipmentById(id);

        assertOwnerOrAdmin(shipment);
        assertEditableStatus(shipment);

        if (request.originAddress() != null) shipment.setOriginAddress(request.originAddress());
        if (request.originLatitude() != null) shipment.setOriginLatitude(request.originLatitude());
        if (request.originLongitude() != null) shipment.setOriginLongitude(request.originLongitude());
        if (request.destinationAddress() != null) shipment.setDestinationAddress(request.destinationAddress());
        if (request.destinationLatitude() != null) shipment.setDestinationLatitude(request.destinationLatitude());
        if (request.destinationLongitude() != null) shipment.setDestinationLongitude(request.destinationLongitude());
        if (request.cargoType() != null) {
            validateCategory(request.cargoType());
            shipment.setCargoType(request.cargoType());
        }
        if (request.totalWeight() != null) {
            validateWeight(request.totalWeight());
            shipment.setTotalWeight(request.totalWeight());
        }
        if (request.totalVolume() != null) shipment.setTotalVolume(request.totalVolume());
        if (request.description() != null) shipment.setDescription(request.description());
        if (request.budgetAmount() != null) shipment.setBudgetAmount(request.budgetAmount());
        if (request.budgetCurrency() != null) shipment.setBudgetCurrency(request.budgetCurrency());
        if (request.requiredTruckType() != null) shipment.setRequiredTruckType(request.requiredTruckType());
        if (request.expiresAt() != null) shipment.setExpiresAt(request.expiresAt());

        Shipment saved = shipmentRepository.save(shipment);
        eventPublisher.publishShipmentUpdated(saved.getId(), saved.getBusinessOwnerId(),
                saved.getStatus().name(), saved.getStatus().name());

        return shipmentMapper.toResponse(saved);
    }

    // ─── Status Management ─────────────────────────────────────────────────────

    @Transactional
    public ShipmentDto.Response updateStatus(UUID id, ShipmentDto.StatusUpdateRequest request) {
        log.info("Updating status of shipment: {} to {}", id, request.newStatus());
        Shipment shipment = findShipmentById(id);
        assertOwnerOrAdmin(shipment);

        ShipmentStatus currentStatus = shipment.getStatus();
        ShipmentStatus newStatus = request.newStatus();

        Set<ShipmentStatus> allowed = VALID_TRANSITIONS.getOrDefault(currentStatus, Set.of());
        if (!allowed.contains(newStatus)) {
            throw new InvalidShipmentStateException(
                    String.format("Cannot transition from %s to %s", currentStatus, newStatus));
        }

        UUID changedBy = getCurrentUserId();

        ShipmentStatusHistory history = ShipmentStatusHistory.builder()
                .shipment(shipment)
                .oldStatus(currentStatus)
                .newStatus(newStatus)
                .changedAt(LocalDateTime.now())
                .changedBy(changedBy)
                .remarks(request.remarks())
                .build();
        statusHistoryRepository.save(history);

        shipment.setStatus(newStatus);
        Shipment saved = shipmentRepository.save(shipment);

        // Publish appropriate events
        switch (newStatus) {
            case AVAILABLE -> eventPublisher.publishShipmentReadyForMatching(
                    saved.getId(), saved.getBusinessOwnerId(),
                    saved.getOriginAddress(), saved.getOriginLatitude(), saved.getOriginLongitude(),
                    saved.getDestinationAddress(), saved.getDestinationLatitude(), saved.getDestinationLongitude(),
                    saved.getTotalWeight(), saved.getTotalVolume(), saved.getCargoType(),
                    saved.getRequiredTruckType(), saved.getBudgetAmount());
            case CANCELLED -> eventPublisher.publishShipmentCancelled(saved.getId(), saved.getBusinessOwnerId(), request.remarks());
            case DELIVERED -> eventPublisher.publishShipmentDelivered(saved.getId(), saved.getBusinessOwnerId());
            case COMPLETED -> eventPublisher.publishShipmentCompleted(saved.getId(), saved.getBusinessOwnerId());
            case EXPIRED -> eventPublisher.publishShipmentExpired(saved.getId(), saved.getBusinessOwnerId());
            default -> log.debug("No event published for status: {}", newStatus);
        }

        return shipmentMapper.toResponse(saved);
    }

    @Transactional
    public ShipmentDto.Response cancelShipment(UUID id, String remarks) {
        ShipmentDto.StatusUpdateRequest request = new ShipmentDto.StatusUpdateRequest(ShipmentStatus.CANCELLED, remarks);
        return updateStatus(id, request);
    }

    public List<ShipmentDto.StatusHistoryResponse> getStatusHistory(UUID id) {
        findShipmentById(id); // validate existence
        return statusHistoryRepository.findByShipmentIdAndIsDeletedFalseOrderByChangedAtDesc(id)
                .stream().map(shipmentMapper::toStatusHistoryResponse).collect(Collectors.toList());
    }

    public List<ShipmentDto.CategoryResponse> getActiveCategories() {
        return categoryRepository.findByActiveTrueAndIsDeletedFalse()
                .stream().map(shipmentMapper::toCategoryResponse).collect(Collectors.toList());
    }

    public ShipmentDto.TrackingResponse trackShipment(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumberAndIsDeletedFalse(trackingNumber)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found: " + trackingNumber));

        List<ShipmentDto.StatusHistoryResponse> history =
                statusHistoryRepository.findByShipmentIdAndIsDeletedFalseOrderByChangedAtDesc(shipment.getId())
                        .stream().map(shipmentMapper::toStatusHistoryResponse).collect(Collectors.toList());

        return new ShipmentDto.TrackingResponse(
                shipment.getTrackingNumber(),
                shipment.getStatus().name(),
                shipment.getPickupDetails() != null ? shipmentMapper.toPickupResponse(shipment.getPickupDetails()) : null,
                shipment.getDropDetails() != null ? shipmentMapper.toDropResponse(shipment.getDropDetails()) : null,
                history
        );
    }

    // ─── Document Management ──────────────────────────────────────────────────

    @Transactional
    public ShipmentDto.DocumentResponse uploadDocument(UUID shipmentId, ShipmentDto.DocumentUploadRequest request) {
        log.info("Uploading document for shipment: {}", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        assertOwnerOrAdmin(shipment);
        validateDocumentType(request.documentType());

        String fileUrl = resolveStorageUrl(DOCUMENT_BUCKET, shipmentId, request.fileName(),
                request.fileUrl(), request.contentType(), request.fileSizeBytes());
        ShipmentDocument doc = ShipmentDocument.builder()
                .shipment(shipment)
                .documentType(request.documentType())
                .documentNumber(request.documentNumber())
                .fileUrl(fileUrl)
                .fileName(request.fileName())
                .contentType(request.contentType())
                .fileSizeBytes(request.fileSizeBytes())
                .expiryDate(request.expiryDate())
                .status("PENDING")
                .build();

        ShipmentDocument saved = documentRepository.save(doc);
        return shipmentMapper.toDocumentResponse(saved);
    }

    public List<ShipmentDto.DocumentResponse> getDocuments(UUID shipmentId) {
        Shipment shipment = findShipmentById(shipmentId);
        assertOwnerOrAdmin(shipment);
        return documentRepository.findByShipmentIdAndIsDeletedFalse(shipmentId)
                .stream().map(shipmentMapper::toDocumentResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteDocument(UUID documentId) {
        ShipmentDocument doc = documentRepository.findByIdAndIsDeletedFalse(documentId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found: " + documentId));
        assertOwnerOrAdmin(doc.getShipment());
        deleteStoredObject(DOCUMENT_BUCKET, doc.getFileName());
        doc.setDeleted(true);
        documentRepository.save(doc);
    }

    // ─── Image Management ─────────────────────────────────────────────────────

    @Transactional
    public ShipmentDto.ImageResponse uploadImage(UUID shipmentId, ShipmentDto.ImageUploadRequest request) {
        log.info("Uploading image for shipment: {}", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        assertOwnerOrAdmin(shipment);

        String fileUrl = resolveStorageUrl(IMAGE_BUCKET, shipmentId, request.fileName(),
                request.fileUrl(), request.contentType(), request.fileSizeBytes());
        ShipmentImage image = ShipmentImage.builder()
                .shipment(shipment)
                .fileUrl(fileUrl)
                .fileName(request.fileName())
                .contentType(request.contentType())
                .fileSizeBytes(request.fileSizeBytes())
                .caption(request.caption())
                .build();

        ShipmentImage saved = imageRepository.save(image);
        return shipmentMapper.toImageResponse(saved);
    }

    public List<ShipmentDto.ImageResponse> getImages(UUID shipmentId) {
        Shipment shipment = findShipmentById(shipmentId);
        assertOwnerOrAdmin(shipment);
        return imageRepository.findByShipmentIdAndIsDeletedFalse(shipmentId)
                .stream().map(shipmentMapper::toImageResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteImage(UUID imageId) {
        ShipmentImage image = imageRepository.findByIdAndIsDeletedFalse(imageId)
                .orElseThrow(() -> new DocumentNotFoundException("Image not found: " + imageId));
        assertOwnerOrAdmin(image.getShipment());
        deleteStoredObject(IMAGE_BUCKET, image.getFileName());
        image.setDeleted(true);
        imageRepository.save(image);
    }

    // ─── Pricing ──────────────────────────────────────────────────────────────

    @Transactional
    public ShipmentDto.PricingResponse updatePricing(UUID shipmentId, ShipmentDto.PricingUpdateRequest request) {
        log.info("Updating pricing for shipment: {}", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        assertOwnerOrAdmin(shipment);
        validatePricing(request);

        ShipmentPricing pricing = pricingRepository.findByShipmentIdAndIsDeletedFalse(shipmentId)
                .orElseGet(() -> ShipmentPricing.builder().shipment(shipment).build());

        if (request.baseRate() != null) pricing.setBaseRate(request.baseRate());
        if (request.distanceCharge() != null) pricing.setDistanceCharge(request.distanceCharge());
        if (request.weightCharge() != null) pricing.setWeightCharge(request.weightCharge());
        if (request.insuranceCharge() != null) pricing.setInsuranceCharge(request.insuranceCharge());
        if (request.taxAmount() != null) pricing.setTaxAmount(request.taxAmount());
        if (request.totalAmount() != null) pricing.setTotalAmount(request.totalAmount());
        if (request.currency() != null) pricing.setCurrency(request.currency());
        if (request.pricingStatus() != null) pricing.setPricingStatus(request.pricingStatus());

        ShipmentPricing saved = pricingRepository.save(pricing);
        return shipmentMapper.toPricingResponse(saved);
    }

    public ShipmentDto.PricingResponse getPricing(UUID shipmentId) {
        Shipment shipment = findShipmentById(shipmentId);
        assertOwnerOrAdmin(shipment);
        ShipmentPricing pricing = pricingRepository.findByShipmentIdAndIsDeletedFalse(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Pricing not found for shipment: " + shipmentId));
        return shipmentMapper.toPricingResponse(pricing);
    }

    @Transactional
    public ShipmentDto.ItemResponse addItem(UUID shipmentId, ShipmentDto.ItemRequest request) {
        log.info("Adding item to shipment: {}", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        assertOwnerOrAdmin(shipment);
        assertEditableStatus(shipment);

        ShipmentItem item = ShipmentItem.builder()
                .shipment(shipment)
                .name(request.name())
                .quantity(request.quantity())
                .weight(request.weight())
                .volume(request.volume())
                .unit(request.unit())
                .description(request.description())
                .value(request.value())
                .currency(request.currency())
                .build();

        ShipmentItem saved = itemRepository.save(item);
        eventPublisher.publishShipmentUpdated(shipment.getId(), shipment.getBusinessOwnerId(),
                shipment.getStatus().name(), shipment.getStatus().name());
        return shipmentMapper.toItemResponse(saved);
    }

    public List<ShipmentDto.ItemResponse> getItems(UUID shipmentId) {
        Shipment shipment = findShipmentById(shipmentId);
        assertOwnerOrAdmin(shipment);
        return itemRepository.findByShipmentIdAndIsDeletedFalse(shipmentId)
                .stream().map(shipmentMapper::toItemResponse).collect(Collectors.toList());
    }

    // ─── Scheduling ───────────────────────────────────────────────────────────

    @Transactional
    public ShipmentDto.PickupResponse schedulePickup(UUID shipmentId, ShipmentDto.PickupScheduleRequest request) {
        log.info("Scheduling pickup for shipment: {}", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        assertOwnerOrAdmin(shipment);

        ShipmentDto.DropScheduleRequest dropReq = null;
        if (shipment.getDropDetails() != null) {
            LocalDateTime dropTime = shipment.getDropDetails().getScheduledAt();
            if (dropTime != null && request.scheduledAt() != null) {
                validateSchedule(request.scheduledAt(), dropTime);
            }
        }

        PickupDetails pickup = pickupDetailsRepository.findByShipmentIdAndIsDeletedFalse(shipmentId)
                .orElseGet(() -> PickupDetails.builder().shipment(shipment).build());

        applyPickupDetails(pickup, request);
        PickupDetails saved = pickupDetailsRepository.save(pickup);
        return shipmentMapper.toPickupResponse(saved);
    }

    @Transactional
    public ShipmentDto.DropResponse scheduleDrop(UUID shipmentId, ShipmentDto.DropScheduleRequest request) {
        log.info("Scheduling drop for shipment: {}", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        assertOwnerOrAdmin(shipment);

        if (shipment.getPickupDetails() != null) {
            LocalDateTime pickupTime = shipment.getPickupDetails().getScheduledAt();
            if (pickupTime != null && request.scheduledAt() != null) {
                validateSchedule(pickupTime, request.scheduledAt());
            }
        }

        DropDetails drop = dropDetailsRepository.findByShipmentIdAndIsDeletedFalse(shipmentId)
                .orElseGet(() -> DropDetails.builder().shipment(shipment).build());

        applyDropDetails(drop, request);
        DropDetails saved = dropDetailsRepository.save(drop);
        return shipmentMapper.toDropResponse(saved);
    }

    // ─── Soft Delete ──────────────────────────────────────────────────────────

    @Transactional
    public void deleteShipment(UUID id) {
        log.info("Soft deleting shipment: {}", id);
        Shipment shipment = findShipmentById(id);
        assertOwnerOrAdmin(shipment);
        assertEditableStatus(shipment);
        shipment.setDeleted(true);
        shipmentRepository.save(shipment);
    }

    @Scheduled(fixedDelayString = "${shipment.expiry-scan-delay-ms:300000}")
    @Transactional
    public void expireOverdueShipments() {
        List<Shipment> expiredShipments = shipmentRepository.findExpiredShipments(LocalDateTime.now());
        for (Shipment shipment : expiredShipments) {
            ShipmentStatus oldStatus = shipment.getStatus();
            shipment.setStatus(ShipmentStatus.EXPIRED);
            Shipment saved = shipmentRepository.save(shipment);
            statusHistoryRepository.save(ShipmentStatusHistory.builder()
                    .shipment(saved)
                    .oldStatus(oldStatus)
                    .newStatus(ShipmentStatus.EXPIRED)
                    .changedAt(LocalDateTime.now())
                    .remarks("Auto-expired by system")
                    .build());
            eventPublisher.publishShipmentExpired(saved.getId(), saved.getBusinessOwnerId());
        }
    }

    // ─── Internal (for Feign) ─────────────────────────────────────────────────

    public Shipment findShipmentById(UUID id) {
        return shipmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found: " + id));
    }

    // ─── Private Helpers ──────────────────────────────────────────────────────

    private void validateBusinessOwner(UUID businessOwnerId) {
        try {
            UserFeignClient.InternalUserResponse user = userFeignClient.getUser(businessOwnerId);
            if (user == null || !"ACTIVE".equalsIgnoreCase(user.status())) {
                throw new BusinessNotFoundException("Business owner not found or inactive: " + businessOwnerId);
            }
        } catch (BusinessNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Could not validate business owner (user-service unavailable): {}", e.getMessage());
            throw new BusinessNotFoundException("Unable to validate business owner: " + businessOwnerId);
        }
    }

    private void validateWeight(Double weight) {
        if (weight == null || weight <= 0) {
            throw new InvalidWeightException("Weight must be greater than zero");
        }
    }

    private void validateDocumentType(String documentType) {
        if (documentType == null || !ALLOWED_DOCUMENT_TYPES.contains(documentType)) {
            throw new InvalidShipmentStateException("Unsupported shipment document type: " + documentType);
        }
    }

    private void validatePricing(ShipmentDto.PricingUpdateRequest request) {
        validateNonNegative(request.baseRate(), "baseRate");
        validateNonNegative(request.distanceCharge(), "distanceCharge");
        validateNonNegative(request.weightCharge(), "weightCharge");
        validateNonNegative(request.insuranceCharge(), "insuranceCharge");
        validateNonNegative(request.taxAmount(), "taxAmount");
        validateNonNegative(request.totalAmount(), "totalAmount");
    }

    private void validateNonNegative(Double value, String fieldName) {
        if (value != null && value < 0) {
            throw new InvalidShipmentStateException(fieldName + " cannot be negative");
        }
    }

    private void validateCategory(String cargoType) {
        if (cargoType == null || cargoType.isBlank()) {
            throw new InvalidShipmentStateException("Shipment category is required");
        }
        ShipmentCategory category = categoryRepository.findByCodeAndIsDeletedFalse(cargoType)
                .orElseThrow(() -> new InvalidShipmentStateException("Invalid shipment category: " + cargoType));
        if (!category.isActive()) {
            throw new InvalidShipmentStateException("Shipment category is inactive: " + cargoType);
        }
    }

    private void validateSchedule(LocalDateTime pickupAt, LocalDateTime dropAt) {
        if (pickupAt != null && dropAt != null && !dropAt.isAfter(pickupAt)) {
            throw new InvalidScheduleException("Delivery time must be after pickup time");
        }
    }

    private void assertEditableStatus(Shipment shipment) {
        if (!Set.of(ShipmentStatus.CREATED, ShipmentStatus.AVAILABLE).contains(shipment.getStatus())) {
            throw new InvalidShipmentStateException(
                    "Shipment can only be edited in CREATED or AVAILABLE status. Current: " + shipment.getStatus());
        }
    }

    private void assertOwnerOrAdmin(Shipment shipment) {
        assertOwnerOrAdmin(shipment.getBusinessOwnerId());
    }

    private void assertOwnerOrAdmin(UUID businessOwnerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authenticated user context is required");
        }
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
        if (admin) {
            return;
        }
        UUID currentUserId = getCurrentUserId();
        if (currentUserId == null || !currentUserId.equals(businessOwnerId)) {
            throw new AccessDeniedException("Only shipment owner or admin can modify this shipment");
        }
    }

    private String generateTrackingNumber() {
        return "SHP" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private UUID getCurrentUserId() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof String principal) {
                return UUID.fromString(principal);
            }
        } catch (Exception e) {
            log.debug("Could not extract user ID from security context");
        }
        return null;
    }

    private String resolveStorageUrl(String bucketName, UUID shipmentId, String fileName,
                                     String requestedUrl, String contentType, Long contentLength) {
        if (requestedUrl != null && !requestedUrl.isBlank()) {
            return requestedUrl;
        }
        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException("Content type is required when storage URL is not provided");
        }
        String key = shipmentId + "/" + (fileName == null || fileName.isBlank() ? UUID.randomUUID().toString() : fileName);
        return storageService.uploadFile(bucketName, key, InputStream.nullInputStream(), contentType,
                contentLength == null ? 0L : contentLength);
    }

    private void deleteStoredObject(String bucketName, String fileName) {
        if (fileName != null && !fileName.isBlank()) {
            storageService.deleteFile(bucketName, fileName);
        }
    }

    private void createPickupDetails(Shipment shipment, ShipmentDto.PickupScheduleRequest req) {
        PickupDetails pickup = PickupDetails.builder().shipment(shipment).build();
        applyPickupDetails(pickup, req);
        pickupDetailsRepository.save(pickup);
    }

    private void createDropDetails(Shipment shipment, ShipmentDto.DropScheduleRequest req) {
        DropDetails drop = DropDetails.builder().shipment(shipment).build();
        applyDropDetails(drop, req);
        dropDetailsRepository.save(drop);
    }

    private void createDimension(Shipment shipment, ShipmentDto.DimensionRequest req) {
        ShipmentDimension dim = ShipmentDimension.builder()
                .shipment(shipment)
                .lengthCm(req.lengthCm())
                .widthCm(req.widthCm())
                .heightCm(req.heightCm())
                .dimensionUnit(req.dimensionUnit() != null ? req.dimensionUnit() : "CM")
                .build();
        if (req.lengthCm() != null && req.widthCm() != null && req.heightCm() != null) {
            dim.setVolumeCbm((req.lengthCm() * req.widthCm() * req.heightCm()) / 1_000_000.0);
        }
        // dimension is saved via cascade
        shipment.setDimension(dim);
    }

    private void createItem(Shipment shipment, ShipmentDto.ItemRequest req) {
        ShipmentItem item = ShipmentItem.builder()
                .shipment(shipment)
                .name(req.name())
                .quantity(req.quantity())
                .weight(req.weight())
                .volume(req.volume())
                .unit(req.unit())
                .description(req.description())
                .value(req.value())
                .currency(req.currency())
                .build();
        shipment.getItems().add(item);
    }

    private void applyPickupDetails(PickupDetails pickup, ShipmentDto.PickupScheduleRequest req) {
        pickup.setAddress(req.address());
        pickup.setLatitude(req.latitude());
        pickup.setLongitude(req.longitude());
        pickup.setCity(req.city());
        pickup.setState(req.state());
        pickup.setPinCode(req.pinCode());
        pickup.setContactName(req.contactName());
        pickup.setContactPhone(req.contactPhone());
        pickup.setScheduledAt(req.scheduledAt());
        pickup.setSpecialInstructions(req.specialInstructions());
    }

    private void applyDropDetails(DropDetails drop, ShipmentDto.DropScheduleRequest req) {
        drop.setAddress(req.address());
        drop.setLatitude(req.latitude());
        drop.setLongitude(req.longitude());
        drop.setCity(req.city());
        drop.setState(req.state());
        drop.setPinCode(req.pinCode());
        drop.setContactName(req.contactName());
        drop.setContactPhone(req.contactPhone());
        drop.setScheduledAt(req.scheduledAt());
        drop.setSpecialInstructions(req.specialInstructions());
    }
}
