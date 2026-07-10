package com.smartlogistics.userservice.config;

import com.smartlogistics.userservice.util.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class UserContextFilter implements Filter {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest httpServletRequest) {
            String userIdStr = httpServletRequest.getHeader(USER_ID_HEADER);
            String requestId = httpServletRequest.getHeader(REQUEST_ID_HEADER);
            String correlationId = httpServletRequest.getHeader(CORRELATION_ID_HEADER);

            if (requestId == null || requestId.trim().isEmpty()) {
                requestId = UUID.randomUUID().toString();
            }
            if (correlationId == null || correlationId.trim().isEmpty()) {
                correlationId = UUID.randomUUID().toString();
            }

            UserContext.setRequestId(requestId);
            UserContext.setCorrelationId(correlationId);
            MDC.put("requestId", requestId);
            MDC.put("correlationId", correlationId);

            if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                try {
                    UUID userId = UUID.fromString(userIdStr.trim());
                    UserContext.setUserId(userId);
                    MDC.put("userId", userId.toString());
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid UUID in {} header: {}", USER_ID_HEADER, userIdStr);
                }
            }

            // Propagate tracing headers in the HTTP response for API clients
            if (response instanceof HttpServletResponse httpServletResponse) {
                httpServletResponse.setHeader(REQUEST_ID_HEADER, requestId);
                httpServletResponse.setHeader(CORRELATION_ID_HEADER, correlationId);
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
            MDC.clear();
        }
    }
}
