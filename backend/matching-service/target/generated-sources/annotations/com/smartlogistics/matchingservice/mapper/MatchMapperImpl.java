package com.smartlogistics.matchingservice.mapper;

import com.smartlogistics.matchingservice.dto.BidDto;
import com.smartlogistics.matchingservice.dto.MatchRequestDto;
import com.smartlogistics.matchingservice.dto.MatchResultDto;
import com.smartlogistics.matchingservice.dto.MatchRuleDto;
import com.smartlogistics.matchingservice.entity.Bid;
import com.smartlogistics.matchingservice.entity.MatchRequest;
import com.smartlogistics.matchingservice.entity.MatchResult;
import com.smartlogistics.matchingservice.entity.MatchRule;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-10T23:33:01+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class MatchMapperImpl implements MatchMapper {

    @Override
    public MatchRequestDto.Response toDto(MatchRequest request) {
        if ( request == null ) {
            return null;
        }

        UUID id = null;
        UUID shipmentId = null;
        UUID businessId = null;
        String status = null;
        Double pickupLatitude = null;
        Double pickupLongitude = null;
        Double destinationLatitude = null;
        Double destinationLongitude = null;
        Double radiusKm = null;
        Double maxDistanceKm = null;
        LocalDateTime expiresAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = request.getId();
        shipmentId = request.getShipmentId();
        businessId = request.getBusinessId();
        status = request.getStatus();
        pickupLatitude = request.getPickupLatitude();
        pickupLongitude = request.getPickupLongitude();
        destinationLatitude = request.getDestinationLatitude();
        destinationLongitude = request.getDestinationLongitude();
        radiusKm = request.getRadiusKm();
        maxDistanceKm = request.getMaxDistanceKm();
        expiresAt = request.getExpiresAt();
        createdAt = request.getCreatedAt();
        updatedAt = request.getUpdatedAt();

        MatchRequestDto.Response response = new MatchRequestDto.Response( id, shipmentId, businessId, status, pickupLatitude, pickupLongitude, destinationLatitude, destinationLongitude, radiusKm, maxDistanceKm, expiresAt, createdAt, updatedAt );

        return response;
    }

    @Override
    public MatchResultDto.Response toDto(MatchResult result) {
        if ( result == null ) {
            return null;
        }

        UUID matchRequestId = null;
        UUID id = null;
        UUID shipmentId = null;
        UUID truckId = null;
        UUID driverId = null;
        Double overallScore = null;
        Double distanceScore = null;
        Double capacityScore = null;
        Double availabilityScore = null;
        Double compatibilityScore = null;
        Double estimatedEtaMinutes = null;
        Double estimatedCost = null;
        String reasoning = null;
        String status = null;
        LocalDateTime expiresAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        matchRequestId = resultMatchRequestId( result );
        id = result.getId();
        shipmentId = result.getShipmentId();
        truckId = result.getTruckId();
        driverId = result.getDriverId();
        overallScore = result.getOverallScore();
        distanceScore = result.getDistanceScore();
        capacityScore = result.getCapacityScore();
        availabilityScore = result.getAvailabilityScore();
        compatibilityScore = result.getCompatibilityScore();
        estimatedEtaMinutes = result.getEstimatedEtaMinutes();
        estimatedCost = result.getEstimatedCost();
        reasoning = result.getReasoning();
        status = result.getStatus();
        expiresAt = result.getExpiresAt();
        createdAt = result.getCreatedAt();
        updatedAt = result.getUpdatedAt();

        MatchResultDto.Response response = new MatchResultDto.Response( id, matchRequestId, shipmentId, truckId, driverId, overallScore, distanceScore, capacityScore, availabilityScore, compatibilityScore, estimatedEtaMinutes, estimatedCost, reasoning, status, expiresAt, createdAt, updatedAt );

        return response;
    }

    @Override
    public BidDto.Response toDto(Bid bid) {
        if ( bid == null ) {
            return null;
        }

        UUID matchResultId = null;
        UUID id = null;
        UUID shipmentId = null;
        UUID truckId = null;
        UUID driverId = null;
        UUID businessId = null;
        Double amount = null;
        String currency = null;
        String status = null;
        String message = null;
        LocalDateTime expiresAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        matchResultId = bidMatchResultId( bid );
        id = bid.getId();
        shipmentId = bid.getShipmentId();
        truckId = bid.getTruckId();
        driverId = bid.getDriverId();
        businessId = bid.getBusinessId();
        amount = bid.getAmount();
        currency = bid.getCurrency();
        status = bid.getStatus();
        message = bid.getMessage();
        expiresAt = bid.getExpiresAt();
        createdAt = bid.getCreatedAt();
        updatedAt = bid.getUpdatedAt();

        BidDto.Response response = new BidDto.Response( id, matchResultId, shipmentId, truckId, driverId, businessId, amount, currency, status, message, expiresAt, createdAt, updatedAt );

        return response;
    }

    @Override
    public MatchRuleDto.Response toDto(MatchRule rule) {
        if ( rule == null ) {
            return null;
        }

        String code = null;
        Double weight = null;
        boolean active = false;
        String description = null;

        code = rule.getCode();
        weight = rule.getWeight();
        active = rule.isActive();
        description = rule.getDescription();

        MatchRuleDto.Response response = new MatchRuleDto.Response( code, weight, active, description );

        return response;
    }

    private UUID resultMatchRequestId(MatchResult matchResult) {
        if ( matchResult == null ) {
            return null;
        }
        MatchRequest matchRequest = matchResult.getMatchRequest();
        if ( matchRequest == null ) {
            return null;
        }
        UUID id = matchRequest.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID bidMatchResultId(Bid bid) {
        if ( bid == null ) {
            return null;
        }
        MatchResult matchResult = bid.getMatchResult();
        if ( matchResult == null ) {
            return null;
        }
        UUID id = matchResult.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
