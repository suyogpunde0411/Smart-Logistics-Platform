package com.smartlogistics.shared.event;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class MatchCreatedEvent extends BaseEvent {
    private UUID matchId;
    private UUID shipmentId;
    private UUID truckId;
    private Double price;

    public MatchCreatedEvent() {}

    public MatchCreatedEvent(UUID matchId, UUID shipmentId, UUID truckId, Double price, String correlationId) {
        super(correlationId);
        this.matchId = matchId;
        this.shipmentId = shipmentId;
        this.truckId = truckId;
        this.price = price;
    }
}
