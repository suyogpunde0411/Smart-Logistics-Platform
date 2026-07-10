package com.smartlogistics.shared.event;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class TruckRegisteredEvent extends BaseEvent {
    private UUID truckId;
    private String registrationNumber;
    private UUID ownerId;

    public TruckRegisteredEvent() {}

    public TruckRegisteredEvent(UUID truckId, String registrationNumber, UUID ownerId, String correlationId) {
        super(correlationId);
        this.truckId = truckId;
        this.registrationNumber = registrationNumber;
        this.ownerId = ownerId;
    }
}
