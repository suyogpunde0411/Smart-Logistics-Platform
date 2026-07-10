package com.smartlogistics.shared.event;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseEvent {
    private UUID eventId = UUID.randomUUID();
    private LocalDateTime timestamp = LocalDateTime.now();
    private String correlationId;
    
    protected BaseEvent() {}
    
    protected BaseEvent(String correlationId) {
        this.correlationId = correlationId;
    }
}
