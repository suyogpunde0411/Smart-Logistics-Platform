package com.smartlogistics.shared.event;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class TripCompletedEvent extends BaseEvent {
    private UUID tripId;
    private UUID truckId;
    private Double distanceTravelled;

    public TripCompletedEvent() {}

    public TripCompletedEvent(UUID tripId, UUID truckId, Double distanceTravelled, String correlationId) {
        super(correlationId);
        this.tripId = tripId;
        this.truckId = truckId;
        this.distanceTravelled = distanceTravelled;
    }
}
