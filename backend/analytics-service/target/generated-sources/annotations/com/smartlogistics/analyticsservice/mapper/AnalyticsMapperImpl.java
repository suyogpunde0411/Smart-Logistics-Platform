package com.smartlogistics.analyticsservice.mapper;

import com.smartlogistics.analyticsservice.dto.TripAnalyticsResponseDto;
import com.smartlogistics.analyticsservice.entity.TripAnalytics;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T20:15:11+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class AnalyticsMapperImpl implements AnalyticsMapper {

    @Override
    public TripAnalyticsResponseDto toDto(TripAnalytics entity) {
        if ( entity == null ) {
            return null;
        }

        UUID tripId = null;
        UUID shipmentId = null;
        UUID driverId = null;
        UUID truckId = null;
        UUID businessId = null;
        String status = null;
        Double distanceKm = null;
        Double durationMinutes = null;
        Double fuelConsumedLiters = null;
        Double carbonSavingsKg = null;
        Boolean isLate = null;
        Boolean isEmpty = null;
        LocalDateTime createdAt = null;

        tripId = entity.getTripId();
        shipmentId = entity.getShipmentId();
        driverId = entity.getDriverId();
        truckId = entity.getTruckId();
        businessId = entity.getBusinessId();
        status = entity.getStatus();
        distanceKm = entity.getDistanceKm();
        durationMinutes = entity.getDurationMinutes();
        fuelConsumedLiters = entity.getFuelConsumedLiters();
        carbonSavingsKg = entity.getCarbonSavingsKg();
        isLate = entity.getIsLate();
        isEmpty = entity.getIsEmpty();
        createdAt = entity.getCreatedAt();

        TripAnalyticsResponseDto tripAnalyticsResponseDto = new TripAnalyticsResponseDto( tripId, shipmentId, driverId, truckId, businessId, status, distanceKm, durationMinutes, fuelConsumedLiters, carbonSavingsKg, isLate, isEmpty, createdAt );

        return tripAnalyticsResponseDto;
    }

    @Override
    public List<TripAnalyticsResponseDto> toDtoList(List<TripAnalytics> entities) {
        if ( entities == null ) {
            return null;
        }

        List<TripAnalyticsResponseDto> list = new ArrayList<TripAnalyticsResponseDto>( entities.size() );
        for ( TripAnalytics tripAnalytics : entities ) {
            list.add( toDto( tripAnalytics ) );
        }

        return list;
    }
}
