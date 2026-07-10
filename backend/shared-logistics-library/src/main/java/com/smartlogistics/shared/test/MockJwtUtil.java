package com.smartlogistics.shared.test;

import org.springframework.http.HttpHeaders;
import java.util.UUID;

public class MockJwtUtil {
    public static HttpHeaders createHeaders(UUID userId, String roles) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", userId.toString());
        headers.set("X-User-Roles", roles);
        headers.set("X-Correlation-Id", UUID.randomUUID().toString());
        headers.set("X-Request-Id", UUID.randomUUID().toString());
        return headers;
    }
}
