package com.smartlogistics.shipmentservice.events;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class ShipmentStatusChangedEvent extends ShipmentBaseEvent {
    private UUID shipmentId;
    private UUID businessOwnerId;
    private String oldStatus;
    private String newStatus;
    private String remarks;

    public ShipmentStatusChangedEvent() {}

    public ShipmentStatusChangedEvent(UUID shipmentId, UUID businessOwnerId,
                                       String oldStatus, String newStatus,
                                       String remarks, String correlationId) {
        super(correlationId);
        this.shipmentId = shipmentId;
        this.businessOwnerId = businessOwnerId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.remarks = remarks;
    }
}
