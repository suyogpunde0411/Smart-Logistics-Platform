package com.smartlogistics.shipmentservice.events;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class ShipmentReadyForMatchingEvent extends ShipmentBaseEvent {
    private UUID shipmentId;
    private UUID businessOwnerId;
    private String originAddress;
    private Double originLatitude;
    private Double originLongitude;
    private String destinationAddress;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private Double totalWeight;
    private Double totalVolume;
    private String cargoType;
    private String requiredTruckType;
    private Double budgetAmount;

    public ShipmentReadyForMatchingEvent() {}

    public ShipmentReadyForMatchingEvent(UUID shipmentId, UUID businessOwnerId,
                                          String originAddress, Double originLat, Double originLng,
                                          String destinationAddress, Double destLat, Double destLng,
                                          Double totalWeight, Double totalVolume, String cargoType,
                                          String requiredTruckType, Double budgetAmount,
                                          String correlationId) {
        super(correlationId);
        this.shipmentId = shipmentId;
        this.businessOwnerId = businessOwnerId;
        this.originAddress = originAddress;
        this.originLatitude = originLat;
        this.originLongitude = originLng;
        this.destinationAddress = destinationAddress;
        this.destinationLatitude = destLat;
        this.destinationLongitude = destLng;
        this.totalWeight = totalWeight;
        this.totalVolume = totalVolume;
        this.cargoType = cargoType;
        this.requiredTruckType = requiredTruckType;
        this.budgetAmount = budgetAmount;
    }
}
