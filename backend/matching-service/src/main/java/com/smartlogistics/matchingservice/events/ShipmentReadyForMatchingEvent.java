package com.smartlogistics.matchingservice.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShipmentReadyForMatchingEvent(
        UUID eventId,
        LocalDateTime timestamp,
        String correlationId,
        UUID shipmentId,
        UUID businessOwnerId,
        String originAddress,
        Double originLatitude,
        Double originLongitude,
        String destinationAddress,
        Double destinationLatitude,
        Double destinationLongitude,
        Double totalWeight,
        Double totalVolume,
        String cargoType,
        String requiredTruckType,
        Double budgetAmount
) {}
