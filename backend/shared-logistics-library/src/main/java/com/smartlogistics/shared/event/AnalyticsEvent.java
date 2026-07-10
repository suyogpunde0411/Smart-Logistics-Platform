package com.smartlogistics.shared.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyticsEvent extends BaseEvent {
    private String eventType;
    private String payload;

    public AnalyticsEvent() {}

    public AnalyticsEvent(String eventType, String payload, String correlationId) {
        super(correlationId);
        this.eventType = eventType;
        this.payload = payload;
    }
}
