package com.smartlogistics.userservice.service.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private String eventId;
    private String eventType;
    private UUID userId;
    private Object payload;
    private Instant timestamp;
}
