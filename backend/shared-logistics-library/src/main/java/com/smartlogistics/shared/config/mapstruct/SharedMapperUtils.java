package com.smartlogistics.shared.config.mapstruct;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class SharedMapperUtils {

    public static LocalDateTime fromEpochMillis(Long millis) {
        if (millis == null) return null;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    public static Long toEpochMillis(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String fromUuid(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    public static UUID toUuid(String uuidStr) {
        return uuidStr != null ? UUID.fromString(uuidStr) : null;
    }
}
