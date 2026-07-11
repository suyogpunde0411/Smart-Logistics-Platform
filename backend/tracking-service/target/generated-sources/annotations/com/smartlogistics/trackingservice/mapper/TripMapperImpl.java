package com.smartlogistics.trackingservice.mapper;

import com.smartlogistics.trackingservice.dto.CheckpointDto;
import com.smartlogistics.trackingservice.dto.FuelLogDto;
import com.smartlogistics.trackingservice.dto.GpsLocationDto;
import com.smartlogistics.trackingservice.dto.RestStopDto;
import com.smartlogistics.trackingservice.dto.TripDelayDto;
import com.smartlogistics.trackingservice.dto.TripDto;
import com.smartlogistics.trackingservice.dto.TripRouteDto;
import com.smartlogistics.trackingservice.dto.TripSummaryDto;
import com.smartlogistics.trackingservice.dto.TripTimelineDto;
import com.smartlogistics.trackingservice.entity.FuelLog;
import com.smartlogistics.trackingservice.entity.GpsLocation;
import com.smartlogistics.trackingservice.entity.RestStop;
import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.entity.TripCheckpoint;
import com.smartlogistics.trackingservice.entity.TripDelay;
import com.smartlogistics.trackingservice.entity.TripRoute;
import com.smartlogistics.trackingservice.entity.TripSummary;
import com.smartlogistics.trackingservice.entity.TripTimeline;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T09:46:29+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class TripMapperImpl implements TripMapper {

    @Override
    public TripDto.Response toResponse(Trip trip) {
        if ( trip == null ) {
            return null;
        }

        UUID id = null;
        UUID shipmentId = null;
        UUID truckId = null;
        UUID driverId = null;
        UUID businessId = null;
        String status = null;
        LocalDateTime startedAt = null;
        LocalDateTime completedAt = null;
        Double totalDistanceTravelledKm = null;
        Double currentLatitude = null;
        Double currentLongitude = null;
        LocalDateTime lastGpsUpdatedAt = null;
        LocalDateTime expectedArrivalTime = null;
        Double remainingDistanceKm = null;
        Double averageSpeedKmh = null;
        TripRouteDto.Response route = null;
        List<CheckpointDto.Response> checkpoints = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = trip.getId();
        shipmentId = trip.getShipmentId();
        truckId = trip.getTruckId();
        driverId = trip.getDriverId();
        businessId = trip.getBusinessId();
        status = trip.getStatus();
        startedAt = trip.getStartedAt();
        completedAt = trip.getCompletedAt();
        totalDistanceTravelledKm = trip.getTotalDistanceTravelledKm();
        currentLatitude = trip.getCurrentLatitude();
        currentLongitude = trip.getCurrentLongitude();
        lastGpsUpdatedAt = trip.getLastGpsUpdatedAt();
        expectedArrivalTime = trip.getExpectedArrivalTime();
        remainingDistanceKm = trip.getRemainingDistanceKm();
        averageSpeedKmh = trip.getAverageSpeedKmh();
        route = toResponse( trip.getRoute() );
        checkpoints = tripCheckpointListToResponseList( trip.getCheckpoints() );
        createdAt = trip.getCreatedAt();
        updatedAt = trip.getUpdatedAt();

        TripDto.Response response = new TripDto.Response( id, shipmentId, truckId, driverId, businessId, status, startedAt, completedAt, totalDistanceTravelledKm, currentLatitude, currentLongitude, lastGpsUpdatedAt, expectedArrivalTime, remainingDistanceKm, averageSpeedKmh, route, checkpoints, createdAt, updatedAt );

        return response;
    }

    @Override
    public TripRouteDto.Response toResponse(TripRoute route) {
        if ( route == null ) {
            return null;
        }

        UUID id = null;
        String startAddress = null;
        Double startLatitude = null;
        Double startLongitude = null;
        String endAddress = null;
        Double endLatitude = null;
        Double endLongitude = null;
        Double plannedDistanceKm = null;
        Double plannedDurationHours = null;

        id = route.getId();
        startAddress = route.getStartAddress();
        startLatitude = route.getStartLatitude();
        startLongitude = route.getStartLongitude();
        endAddress = route.getEndAddress();
        endLatitude = route.getEndLatitude();
        endLongitude = route.getEndLongitude();
        plannedDistanceKm = route.getPlannedDistanceKm();
        plannedDurationHours = route.getPlannedDurationHours();

        TripRouteDto.Response response = new TripRouteDto.Response( id, startAddress, startLatitude, startLongitude, endAddress, endLatitude, endLongitude, plannedDistanceKm, plannedDurationHours );

        return response;
    }

    @Override
    public CheckpointDto.Response toDto(TripCheckpoint checkpoint) {
        if ( checkpoint == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        Double latitude = null;
        Double longitude = null;
        Integer sequenceIndex = null;
        String type = null;
        String status = null;
        LocalDateTime plannedArrivalTime = null;
        LocalDateTime actualArrivalTime = null;
        LocalDateTime departureTime = null;

        id = checkpoint.getId();
        name = checkpoint.getName();
        latitude = checkpoint.getLatitude();
        longitude = checkpoint.getLongitude();
        sequenceIndex = checkpoint.getSequenceIndex();
        type = checkpoint.getType();
        status = checkpoint.getStatus();
        plannedArrivalTime = checkpoint.getPlannedArrivalTime();
        actualArrivalTime = checkpoint.getActualArrivalTime();
        departureTime = checkpoint.getDepartureTime();

        CheckpointDto.Response response = new CheckpointDto.Response( id, name, latitude, longitude, sequenceIndex, type, status, plannedArrivalTime, actualArrivalTime, departureTime );

        return response;
    }

    @Override
    public GpsLocationDto.Response toDto(GpsLocation gpsLocation) {
        if ( gpsLocation == null ) {
            return null;
        }

        UUID id = null;
        Double latitude = null;
        Double longitude = null;
        Double speedKmh = null;
        Double heading = null;
        Double accuracy = null;
        Double altitude = null;
        Double distanceTravelledKm = null;
        LocalDateTime timestamp = null;

        id = gpsLocation.getId();
        latitude = gpsLocation.getLatitude();
        longitude = gpsLocation.getLongitude();
        speedKmh = gpsLocation.getSpeedKmh();
        heading = gpsLocation.getHeading();
        accuracy = gpsLocation.getAccuracy();
        altitude = gpsLocation.getAltitude();
        distanceTravelledKm = gpsLocation.getDistanceTravelledKm();
        timestamp = gpsLocation.getTimestamp();

        GpsLocationDto.Response response = new GpsLocationDto.Response( id, latitude, longitude, speedKmh, heading, accuracy, altitude, distanceTravelledKm, timestamp );

        return response;
    }

    @Override
    public FuelLogDto.Response toDto(FuelLog fuelLog) {
        if ( fuelLog == null ) {
            return null;
        }

        UUID id = null;
        Double fuelVolumeLiters = null;
        Double costPerLiter = null;
        Double totalCost = null;
        Double odometerKm = null;
        String stationName = null;
        String stationLocation = null;
        LocalDateTime refueledAt = null;

        id = fuelLog.getId();
        fuelVolumeLiters = fuelLog.getFuelVolumeLiters();
        costPerLiter = fuelLog.getCostPerLiter();
        totalCost = fuelLog.getTotalCost();
        odometerKm = fuelLog.getOdometerKm();
        stationName = fuelLog.getStationName();
        stationLocation = fuelLog.getStationLocation();
        refueledAt = fuelLog.getRefueledAt();

        FuelLogDto.Response response = new FuelLogDto.Response( id, fuelVolumeLiters, costPerLiter, totalCost, odometerKm, stationName, stationLocation, refueledAt );

        return response;
    }

    @Override
    public RestStopDto.Response toDto(RestStop restStop) {
        if ( restStop == null ) {
            return null;
        }

        UUID id = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Double latitude = null;
        Double longitude = null;
        String stopLocationName = null;
        String notes = null;

        id = restStop.getId();
        startTime = restStop.getStartTime();
        endTime = restStop.getEndTime();
        latitude = restStop.getLatitude();
        longitude = restStop.getLongitude();
        stopLocationName = restStop.getStopLocationName();
        notes = restStop.getNotes();

        RestStopDto.Response response = new RestStopDto.Response( id, startTime, endTime, latitude, longitude, stopLocationName, notes );

        return response;
    }

    @Override
    public TripDelayDto.Response toDto(TripDelay delay) {
        if ( delay == null ) {
            return null;
        }

        UUID id = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Double durationMinutes = null;
        String category = null;
        String reason = null;
        Double latitude = null;
        Double longitude = null;

        id = delay.getId();
        startTime = delay.getStartTime();
        endTime = delay.getEndTime();
        durationMinutes = delay.getDurationMinutes();
        category = delay.getCategory();
        reason = delay.getReason();
        latitude = delay.getLatitude();
        longitude = delay.getLongitude();

        TripDelayDto.Response response = new TripDelayDto.Response( id, startTime, endTime, durationMinutes, category, reason, latitude, longitude );

        return response;
    }

    @Override
    public TripSummaryDto.Response toDto(TripSummary summary) {
        if ( summary == null ) {
            return null;
        }

        UUID id = null;
        LocalDateTime startActualTime = null;
        LocalDateTime endActualTime = null;
        Double durationHours = null;
        Double totalDistanceKm = null;
        Double averageSpeedKmh = null;
        Double totalFuelVolumeLiters = null;
        Double totalFuelCost = null;
        Integer delayCount = null;
        Double totalDelayMinutes = null;
        Integer restStopCount = null;
        Double totalRestMinutes = null;

        id = summary.getId();
        startActualTime = summary.getStartActualTime();
        endActualTime = summary.getEndActualTime();
        durationHours = summary.getDurationHours();
        totalDistanceKm = summary.getTotalDistanceKm();
        averageSpeedKmh = summary.getAverageSpeedKmh();
        totalFuelVolumeLiters = summary.getTotalFuelVolumeLiters();
        totalFuelCost = summary.getTotalFuelCost();
        delayCount = summary.getDelayCount();
        totalDelayMinutes = summary.getTotalDelayMinutes();
        restStopCount = summary.getRestStopCount();
        totalRestMinutes = summary.getTotalRestMinutes();

        TripSummaryDto.Response response = new TripSummaryDto.Response( id, startActualTime, endActualTime, durationHours, totalDistanceKm, averageSpeedKmh, totalFuelVolumeLiters, totalFuelCost, delayCount, totalDelayMinutes, restStopCount, totalRestMinutes );

        return response;
    }

    @Override
    public TripTimelineDto.Response toDto(TripTimeline timeline) {
        if ( timeline == null ) {
            return null;
        }

        UUID id = null;
        UUID tripId = null;
        LocalDateTime timestamp = null;
        String title = null;
        String description = null;
        Double latitude = null;
        Double longitude = null;
        String eventType = null;

        id = timeline.getId();
        tripId = timeline.getTripId();
        timestamp = timeline.getTimestamp();
        title = timeline.getTitle();
        description = timeline.getDescription();
        latitude = timeline.getLatitude();
        longitude = timeline.getLongitude();
        eventType = timeline.getEventType();

        TripTimelineDto.Response response = new TripTimelineDto.Response( id, tripId, timestamp, title, description, latitude, longitude, eventType );

        return response;
    }

    @Override
    public TripRoute toEntity(TripRouteDto.CreateRequest request) {
        if ( request == null ) {
            return null;
        }

        TripRoute.TripRouteBuilder tripRoute = TripRoute.builder();

        tripRoute.startAddress( request.startAddress() );
        tripRoute.startLatitude( request.startLatitude() );
        tripRoute.startLongitude( request.startLongitude() );
        tripRoute.endAddress( request.endAddress() );
        tripRoute.endLatitude( request.endLatitude() );
        tripRoute.endLongitude( request.endLongitude() );
        tripRoute.plannedDistanceKm( request.plannedDistanceKm() );
        tripRoute.plannedDurationHours( request.plannedDurationHours() );

        return tripRoute.build();
    }

    @Override
    public TripCheckpoint toEntity(CheckpointDto.CreateRequest request) {
        if ( request == null ) {
            return null;
        }

        TripCheckpoint.TripCheckpointBuilder tripCheckpoint = TripCheckpoint.builder();

        tripCheckpoint.name( request.name() );
        tripCheckpoint.latitude( request.latitude() );
        tripCheckpoint.longitude( request.longitude() );
        tripCheckpoint.sequenceIndex( request.sequenceIndex() );
        tripCheckpoint.type( request.type() );
        tripCheckpoint.status( request.status() );
        tripCheckpoint.plannedArrivalTime( request.plannedArrivalTime() );

        return tripCheckpoint.build();
    }

    @Override
    public GpsLocation toEntity(GpsLocationDto.CreateRequest request) {
        if ( request == null ) {
            return null;
        }

        GpsLocation.GpsLocationBuilder gpsLocation = GpsLocation.builder();

        gpsLocation.latitude( request.latitude() );
        gpsLocation.longitude( request.longitude() );
        gpsLocation.speedKmh( request.speedKmh() );
        gpsLocation.heading( request.heading() );
        gpsLocation.accuracy( request.accuracy() );
        gpsLocation.altitude( request.altitude() );
        gpsLocation.timestamp( request.timestamp() );

        return gpsLocation.build();
    }

    protected List<CheckpointDto.Response> tripCheckpointListToResponseList(List<TripCheckpoint> list) {
        if ( list == null ) {
            return null;
        }

        List<CheckpointDto.Response> list1 = new ArrayList<CheckpointDto.Response>( list.size() );
        for ( TripCheckpoint tripCheckpoint : list ) {
            list1.add( toDto( tripCheckpoint ) );
        }

        return list1;
    }
}
