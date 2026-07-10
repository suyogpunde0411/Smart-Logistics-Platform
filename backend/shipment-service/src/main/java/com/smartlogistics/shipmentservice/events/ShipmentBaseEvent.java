package com.smartlogistics.shipmentservice.events;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class ShipmentBaseEvent {
    private UUID eventId = UUID.randomUUID();
    private LocalDateTime timestamp = LocalDateTime.now();
    private String correlationId;

    protected ShipmentBaseEvent() {}

    protected ShipmentBaseEvent(String correlationId) {
        this.correlationId = correlationId;
    }
}
