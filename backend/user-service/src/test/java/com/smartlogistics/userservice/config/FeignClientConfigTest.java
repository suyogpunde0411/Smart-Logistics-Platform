package com.smartlogistics.userservice.config;

import com.smartlogistics.userservice.util.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FeignClientConfigTest {

    @Test
    void testRequestInterceptorPropagatesHeaders() {
        UUID mockUserId = UUID.randomUUID();
        UserContext.setUserId(mockUserId);
        UserContext.setRequestId("req-123");
        UserContext.setCorrelationId("corr-456");

        FeignClientConfig config = new FeignClientConfig();
        RequestInterceptor interceptor = config.requestInterceptor();
        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        Map<String, Collection<String>> headers = template.headers();
        assertTrue(headers.containsKey("X-User-Id"));
        assertTrue(headers.containsKey("X-Request-Id"));
        assertTrue(headers.containsKey("X-Correlation-Id"));

        assertEquals(mockUserId.toString(), headers.get("X-User-Id").iterator().next());
        assertEquals("req-123", headers.get("X-Request-Id").iterator().next());
        assertEquals("corr-456", headers.get("X-Correlation-Id").iterator().next());

        UserContext.clear();
    }
}
