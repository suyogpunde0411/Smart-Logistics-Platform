package com.smartlogistics.shipmentservice.events;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class ShipmentCreatedKafkaEvent extends ShipmentBaseEvent {
    private UUID shipmentId;
    private UUID businessOwnerId;
    private String originAddress;
    private String destinationAddress;
    private Double totalWeight;
    private String cargoType;
    private Double budgetAmount;

    public ShipmentCreatedKafkaEvent() {}

    public ShipmentCreatedKafkaEvent(UUID shipmentId, UUID businessOwnerId,
                                     String originAddress, String destinationAddress,
                                     Double totalWeight, String cargoType,
                                     Double budgetAmount, String correlationId) {
        super(correlationId);
        this.shipmentId = shipmentId;
        this.businessOwnerId = businessOwnerId;
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.totalWeight = totalWeight;
        this.cargoType = cargoType;
        this.budgetAmount = budgetAmount;
    }
}
