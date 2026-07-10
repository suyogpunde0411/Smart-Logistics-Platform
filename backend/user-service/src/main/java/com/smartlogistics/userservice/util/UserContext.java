package com.smartlogistics.userservice.util;

import java.util.UUID;

public class UserContext {
    private static final ThreadLocal<UUID> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> CORRELATION_ID = new ThreadLocal<>();

    public static void setUserId(UUID userId) {
        USER_ID.set(userId);
    }

    public static UUID getUserId() {
        return USER_ID.get();
    }

    public static void setRequestId(String requestId) {
        REQUEST_ID.set(requestId);
    }

    public static String getRequestId() {
        return REQUEST_ID.get();
    }

    public static void setCorrelationId(String correlationId) {
        CORRELATION_ID.set(correlationId);
    }

    public static String getCorrelationId() {
        return CORRELATION_ID.get();
    }

    public static void clear() {
        USER_ID.remove();
        REQUEST_ID.remove();
        CORRELATION_ID.remove();
    }
}
