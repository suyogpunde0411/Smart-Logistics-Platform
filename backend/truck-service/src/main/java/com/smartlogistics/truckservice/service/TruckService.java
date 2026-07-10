package com.smartlogistics.truckservice.service;

import com.smartlogistics.common.client.UserFeignClient;
import com.smartlogistics.shared.dto.TruckDTO;
import com.smartlogistics.truckservice.entity.*;
import com.smartlogistics.truckservice.events.TruckEventPublisher;
import com.smartlogistics.truckservice.exception.*;
import com.smartlogistics.truckservice.mapper.TruckMapper;
import com.smartlogistics.truckservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TruckService {

    private final TruckRepository truckRepository;
    private final TruckCapacityRepository capacityRepository;
    private final TruckAvailabilityRepository availabilityRepository;
    private final TruckLocationRepository locationRepository;
    private final TruckLocationHistoryRepository locationHistoryRepository;
    private final TruckDocumentRepository documentRepository;
    private final TruckInsuranceRepository insuranceRepository;
    private final TruckMaintenanceRepository maintenanceRepository;
    private final TruckImageRepository imageRepository;
    private final TruckStatusHistoryRepository statusHistoryRepository;

    private final TruckMapper truckMapper;
    private final TruckEventPublisher eventPublisher;
    private final UserFeignClient userFeignClient;

    @Transactional
    public TruckDTO.Response registerTruck(TruckDTO.RegisterRequest request) {
        log.info("Registering truck: {}", request.registrationNumber());

        if (truckRepository.existsByRegistrationNumberAndIsDeletedFalse(request.registrationNumber())) {
            throw new DuplicateRegistrationNumberException("Truck already registered with number: " + request.registrationNumber());
        }

        // Validate owner exists in user-service via Feign
        try {
            UserFeignClient.InternalUserResponse owner = userFeignClient.getUser(request.ownerId());
            if (owner == null) {
                throw new IllegalArgumentException("Owner profile not found in user service");
            }
        } catch (Exception e) {
            log.error("Failed to validate owner: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid owner credentials or owner service unreachable");
        }

        Truck truck = Truck.builder()
                .registrationNumber(request.registrationNumber())
                .ownerId(request.ownerId())
                .status("ACTIVE")
                .build();

        TruckCapacity capacity = TruckCapacity.builder()
                .truck(truck)
                .maxWeight(request.maxWeight())
                .maxVolume(request.maxVolume())
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
                .latitude(0.0)
                .longitude(0.0)
                .timestamp(LocalDateTime.now())
                .build();
        truck.setLocation(location);

        Truck savedTruck = truckRepository.save(truck);

        eventPublisher.publishTruckRegistered(
                savedTruck.getId(),
                savedTruck.getOwnerId(),
                savedTruck.getRegistrationNumber(),
                "CONTAINER",
                capacity.getMaxWeight()
        );

        return truckMapper.toResponse(savedTruck);
    }

    @Transactional
    public TruckDTO.Response updateTruck(UUID id, TruckDTO.UpdateRequest request) {
        log.info("Updating capacity of truck ID: {}", id);
        Truck truck = truckRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TruckNotFoundException("Truck not found with ID: " + id));

        if (request.maxWeight() <= 0 || request.maxVolume() <= 0) {
            throw new InvalidCapacityException("Capacities must be greater than zero");
        }

        TruckCapacity capacity = truck.getCapacity();
        if (capacity == null) {
            capacity = new TruckCapacity();
            capacity.setTruck(truck);
        }
        capacity.setMaxWeight(request.maxWeight());
        capacity.setMaxVolume(request.maxVolume());
        capacityRepository.save(capacity);

        Truck savedTruck = truckRepository.save(truck);

        eventPublisher.publishTruckUpdated(
                savedTruck.getId(),
                savedTruck.getRegistrationNumber(),
                "CONTAINER",
                capacity.getMaxWeight()
        );

        return truckMapper.toResponse(savedTruck);
    }

    @Transactional
    public void deleteTruck(UUID id) {
        log.info("Soft deleting truck ID: {}", id);
        Truck truck = truckRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TruckNotFoundException("Truck not found with ID: " + id));

        truck.setDeleted(true);
        if (truck.getCapacity() != null) truck.getCapacity().setDeleted(true);
        if (truck.getAvailability() != null) truck.getAvailability().setDeleted(true);
        if (truck.getLocation() != null) truck.getLocation().setDeleted(true);

        truckRepository.save(truck);

        eventPublisher.publishTruckDeleted(id);
    }

    public TruckDTO.Response getTruck(UUID id) {
        Truck truck = truckRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TruckNotFoundException("Truck not found with ID: " + id));
        return truckMapper.toResponse(truck);
    }

    public Page<TruckDTO.Response> searchTrucks(
            String regNum, UUID ownerId, String status, String availStatus, Boolean active, Double minWeight, Double minVolume, Pageable pageable) {
        return truckRepository.searchTrucks(regNum, ownerId, status, availStatus, active, minWeight, minVolume, pageable)
                .map(truckMapper::toResponse);
    }

    @Transactional
    public TruckDTO.AvailabilityDto toggleAvailability(UUID id, TruckDTO.AvailabilityToggle request) {
        log.info("Toggling availability for truck ID: {}", id);
        Truck truck = truckRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TruckNotFoundException("Truck not found with ID: " + id));

        TruckAvailability availability = truck.getAvailability();
        if (availability == null) {
            availability = new TruckAvailability();
            availability.setTruck(truck);
        }
        availability.setActive(request.active());
        availability.setStatus(request.status());
        availabilityRepository.save(availability);

        eventPublisher.publishTruckAvailabilityChanged(
                truck.getId(),
                availability.getStatus(),
                truck.getLocation() != null ? truck.getLocation().getLatitude() : 0.0,
                truck.getLocation() != null ? truck.getLocation().getLongitude() : 0.0
        );

        return truckMapper.toAvailabilityDto(availability);
    }

    @Transactional
    public TruckDTO.LocationDto updateLocation(UUID id, TruckDTO.LocationDto request) {
        log.info("Updating location for truck ID: {}", id);
        Truck truck = truckRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TruckNotFoundException("Truck not found with ID: " + id));

        TruckLocation location = truck.getLocation();
        if (location == null) {
            location = new TruckLocation();
            location.setTruck(truck);
        }
        location.setLatitude(request.latitude());
        location.setLongitude(request.longitude());
        location.setSpeed(request.speed());
        location.setHeading(request.heading());
        location.setAccuracy(request.accuracy());
        location.setTimestamp(LocalDateTime.now());

        locationRepository.save(location);

        // Record history log
        TruckLocationHistory history = TruckLocationHistory.builder()
                .truck(truck)
                .latitude(request.latitude())
                .longitude(request.longitude())
                .speed(request.speed())
                .heading(request.heading())
                .accuracy(request.accuracy())
                .timestamp(LocalDateTime.now())
                .build();
        locationHistoryRepository.save(history);

        eventPublisher.publishTruckLocationUpdated(
                truck.getId(),
                location.getLatitude(),
                location.getLongitude(),
                location.getSpeed(),
                location.getHeading()
        );

        return truckMapper.toLocationDto(location);
    }

    public Page<TruckDTO.Response> findNearbyTrucks(Double lat, Double lng, Double radius, Pageable pageable) {
        return truckRepository.findNearbyAvailableTrucks(lat, lng, radius, pageable)
                .map(truckMapper::toResponse);
    }

    @Transactional
    public TruckDTO.DocumentDto uploadDocument(UUID id, TruckDTO.DocumentUploadRequest request) {
        log.info("Uploading document for truck ID: {}", id);
        Truck truck = truckRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TruckNotFoundException("Truck not found with ID: " + id));

        TruckDocument doc = TruckDocument.builder()
                .truck(truck)
                .type(request.type())
                .documentNumber(request.documentNumber())
                .expiryDate(request.expiryDate())
                .url(request.url())
                .status("PENDING")
                .build();

        TruckDocument savedDoc = documentRepository.save(doc);
        return truckMapper.toDocumentDto(savedDoc);
    }

    @Transactional
    public void deleteDocument(UUID docId) {
        log.info("Deleting document ID: {}", docId);
        TruckDocument doc = documentRepository.findByIdAndIsDeletedFalse(docId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with ID: " + docId));

        doc.setDeleted(true);
        documentRepository.save(doc);
    }

    @Transactional
    public TruckDTO.InsuranceDto addInsurance(UUID id, TruckDTO.InsuranceCreateRequest request) {
        log.info("Adding insurance for truck ID: {}", id);
        Truck truck = truckRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TruckNotFoundException("Truck not found with ID: " + id));

        if (request.expiryDate().isBefore(LocalDate.now())) {
            eventPublisher.publishTruckInsuranceExpired(id, request.policyNumber(), request.expiryDate().toString());
            throw new InsuranceExpiredException("Insurance is already expired");
        }

        TruckInsurance insurance = truck.getInsurance();
        if (insurance == null) {
            insurance = new TruckInsurance();
            insurance.setTruck(truck);
        }
        insurance.setPolicyNumber(request.policyNumber());
        insurance.setProvider(request.provider());
        insurance.setExpiryDate(request.expiryDate());
        insurance.setInsuredAmount(request.insuredAmount());
        insurance.setUrl(request.url());

        insuranceRepository.save(insurance);

        return truckMapper.toInsuranceDto(insurance);
    }

    @Transactional
    public TruckDTO.MaintenanceDto addMaintenanceLog(UUID id, TruckDTO.MaintenanceCreateRequest request) {
        log.info("Adding maintenance log for truck ID: {}", id);
        Truck truck = truckRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TruckNotFoundException("Truck not found with ID: " + id));

        TruckMaintenance maintenance = TruckMaintenance.builder()
                .truck(truck)
                .maintenanceDate(request.maintenanceDate())
                .description(request.description())
                .cost(request.cost())
                .status(request.status())
                .performedBy(request.performedBy())
                .build();

        TruckMaintenance saved = maintenanceRepository.save(maintenance);

        if ("SCHEDULED".equals(request.status())) {
            eventPublisher.publishTruckMaintenanceScheduled(id, saved.getId(), request.maintenanceDate().toString(), request.cost());
        }

        return truckMapper.toMaintenanceDto(saved);
    }
}
