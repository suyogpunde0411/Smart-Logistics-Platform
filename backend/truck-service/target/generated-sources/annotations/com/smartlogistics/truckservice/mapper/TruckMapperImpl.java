package com.smartlogistics.truckservice.mapper;

import com.smartlogistics.shared.dto.TruckDTO;
import com.smartlogistics.truckservice.entity.Truck;
import com.smartlogistics.truckservice.entity.TruckAvailability;
import com.smartlogistics.truckservice.entity.TruckCapacity;
import com.smartlogistics.truckservice.entity.TruckDocument;
import com.smartlogistics.truckservice.entity.TruckImage;
import com.smartlogistics.truckservice.entity.TruckInsurance;
import com.smartlogistics.truckservice.entity.TruckLocation;
import com.smartlogistics.truckservice.entity.TruckMaintenance;
import com.smartlogistics.truckservice.entity.TruckStatusHistory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T09:46:12+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class TruckMapperImpl implements TruckMapper {

    @Override
    public TruckDTO.Response toResponse(Truck truck) {
        if ( truck == null ) {
            return null;
        }

        UUID id = null;
        String registrationNumber = null;
        UUID ownerId = null;
        String status = null;
        TruckDTO.CapacityDto capacity = null;
        TruckDTO.AvailabilityDto availability = null;
        TruckDTO.LocationDto location = null;
        TruckDTO.InsuranceDto insurance = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = truck.getId();
        registrationNumber = truck.getRegistrationNumber();
        ownerId = truck.getOwnerId();
        status = truck.getStatus();
        capacity = toCapacityDto( truck.getCapacity() );
        availability = toAvailabilityDto( truck.getAvailability() );
        location = toLocationDto( truck.getLocation() );
        insurance = toInsuranceDto( truck.getInsurance() );
        createdAt = truck.getCreatedAt();
        updatedAt = truck.getUpdatedAt();

        TruckDTO.Response response = new TruckDTO.Response( id, registrationNumber, ownerId, status, capacity, availability, location, insurance, createdAt, updatedAt );

        return response;
    }

    @Override
    public TruckDTO.CapacityDto toCapacityDto(TruckCapacity capacity) {
        if ( capacity == null ) {
            return null;
        }

        Double maxWeight = null;
        Double maxVolume = null;

        maxWeight = capacity.getMaxWeight();
        maxVolume = capacity.getMaxVolume();

        TruckDTO.CapacityDto capacityDto = new TruckDTO.CapacityDto( maxWeight, maxVolume );

        return capacityDto;
    }

    @Override
    public TruckDTO.AvailabilityDto toAvailabilityDto(TruckAvailability availability) {
        if ( availability == null ) {
            return null;
        }

        String status = null;
        boolean active = false;

        status = availability.getStatus();
        active = availability.isActive();

        TruckDTO.AvailabilityDto availabilityDto = new TruckDTO.AvailabilityDto( status, active );

        return availabilityDto;
    }

    @Override
    public TruckDTO.LocationDto toLocationDto(TruckLocation location) {
        if ( location == null ) {
            return null;
        }

        Double latitude = null;
        Double longitude = null;
        Double speed = null;
        Double heading = null;
        Double accuracy = null;
        LocalDateTime timestamp = null;

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        speed = location.getSpeed();
        heading = location.getHeading();
        accuracy = location.getAccuracy();
        timestamp = location.getTimestamp();

        TruckDTO.LocationDto locationDto = new TruckDTO.LocationDto( latitude, longitude, speed, heading, accuracy, timestamp );

        return locationDto;
    }

    @Override
    public TruckDTO.DocumentDto toDocumentDto(TruckDocument document) {
        if ( document == null ) {
            return null;
        }

        UUID id = null;
        String type = null;
        String documentNumber = null;
        LocalDate expiryDate = null;
        String url = null;
        String status = null;

        id = document.getId();
        type = document.getType();
        documentNumber = document.getDocumentNumber();
        expiryDate = document.getExpiryDate();
        url = document.getUrl();
        status = document.getStatus();

        TruckDTO.DocumentDto documentDto = new TruckDTO.DocumentDto( id, type, documentNumber, expiryDate, url, status );

        return documentDto;
    }

    @Override
    public TruckDTO.InsuranceDto toInsuranceDto(TruckInsurance insurance) {
        if ( insurance == null ) {
            return null;
        }

        UUID id = null;
        String policyNumber = null;
        String provider = null;
        LocalDate expiryDate = null;
        Double insuredAmount = null;
        String url = null;

        id = insurance.getId();
        policyNumber = insurance.getPolicyNumber();
        provider = insurance.getProvider();
        expiryDate = insurance.getExpiryDate();
        insuredAmount = insurance.getInsuredAmount();
        url = insurance.getUrl();

        TruckDTO.InsuranceDto insuranceDto = new TruckDTO.InsuranceDto( id, policyNumber, provider, expiryDate, insuredAmount, url );

        return insuranceDto;
    }

    @Override
    public TruckDTO.MaintenanceDto toMaintenanceDto(TruckMaintenance maintenance) {
        if ( maintenance == null ) {
            return null;
        }

        UUID id = null;
        LocalDate maintenanceDate = null;
        String description = null;
        Double cost = null;
        String status = null;
        String performedBy = null;

        id = maintenance.getId();
        maintenanceDate = maintenance.getMaintenanceDate();
        description = maintenance.getDescription();
        cost = maintenance.getCost();
        status = maintenance.getStatus();
        performedBy = maintenance.getPerformedBy();

        TruckDTO.MaintenanceDto maintenanceDto = new TruckDTO.MaintenanceDto( id, maintenanceDate, description, cost, status, performedBy );

        return maintenanceDto;
    }

    @Override
    public TruckDTO.ImageDto toImageDto(TruckImage image) {
        if ( image == null ) {
            return null;
        }

        UUID id = null;
        String url = null;
        String contentType = null;

        id = image.getId();
        url = image.getUrl();
        contentType = image.getContentType();

        TruckDTO.ImageDto imageDto = new TruckDTO.ImageDto( id, url, contentType );

        return imageDto;
    }

    @Override
    public TruckDTO.StatusHistoryDto toStatusHistoryDto(TruckStatusHistory history) {
        if ( history == null ) {
            return null;
        }

        String oldStatus = null;
        String newStatus = null;
        UUID changedBy = null;
        LocalDateTime changedAt = null;
        String remarks = null;

        oldStatus = history.getOldStatus();
        newStatus = history.getNewStatus();
        changedBy = history.getChangedBy();
        changedAt = history.getChangedAt();
        remarks = history.getRemarks();

        TruckDTO.StatusHistoryDto statusHistoryDto = new TruckDTO.StatusHistoryDto( oldStatus, newStatus, changedBy, changedAt, remarks );

        return statusHistoryDto;
    }
}
