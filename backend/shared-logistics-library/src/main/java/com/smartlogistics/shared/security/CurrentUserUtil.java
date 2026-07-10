package com.smartlogistics.shared.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.UUID;

public class CurrentUserUtil {
    private static final ThreadLocal<UUID> USER_ID_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUserId(UUID userId) {
        USER_ID_THREAD_LOCAL.set(userId);
    }

    public static UUID getUserId() {
        UUID threadUserId = USER_ID_THREAD_LOCAL.get();
        if (threadUserId != null) return threadUserId;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String principalStr) {
            try {
                return UUID.fromString(principalStr);
            } catch (IllegalArgumentException e) {
                // not a valid UUID format
            }
        }
        return null;
    }

    public static void clear() {
        USER_ID_THREAD_LOCAL.remove();
    }
}
