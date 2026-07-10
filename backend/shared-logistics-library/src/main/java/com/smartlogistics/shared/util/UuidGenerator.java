package com.smartlogistics.shared.util;

import java.util.UUID;

public class UuidGenerator {
    public static UUID generate() {
        return UUID.randomUUID();
    }
    public static UUID fromString(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) return null;
        return UUID.fromString(uuid.trim());
    }
}
