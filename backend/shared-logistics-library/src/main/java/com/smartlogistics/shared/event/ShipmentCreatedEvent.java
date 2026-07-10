package com.smartlogistics.shared.event;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class ShipmentCreatedEvent extends BaseEvent {
    private UUID shipmentId;
    private UUID ownerId;
    private Double weight;
    private Double volume;

    public ShipmentCreatedEvent() {}

    public ShipmentCreatedEvent(UUID shipmentId, UUID ownerId, Double weight, Double volume, String correlationId) {
        super(correlationId);
        this.shipmentId = shipmentId;
        this.ownerId = ownerId;
        this.weight = weight;
        this.volume = volume;
    }
}
