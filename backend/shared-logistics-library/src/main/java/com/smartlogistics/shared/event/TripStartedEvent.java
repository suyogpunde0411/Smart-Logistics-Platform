package com.smartlogistics.shared.event;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class TripStartedEvent extends BaseEvent {
    private UUID tripId;
    private UUID truckId;
    private UUID driverId;

    public TripStartedEvent() {}

    public TripStartedEvent(UUID tripId, UUID truckId, UUID driverId, String correlationId) {
        super(correlationId);
        this.tripId = tripId;
        this.truckId = truckId;
        this.driverId = driverId;
    }
}
