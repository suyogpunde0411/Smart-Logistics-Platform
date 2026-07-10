package com.smartlogistics.shared.event;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class NotificationEvent extends BaseEvent {
    private UUID recipientId;
    private String title;
    private String message;

    public NotificationEvent() {}

    public NotificationEvent(UUID recipientId, String title, String message, String correlationId) {
        super(correlationId);
        this.recipientId = recipientId;
        this.title = title;
        this.message = message;
    }
}
