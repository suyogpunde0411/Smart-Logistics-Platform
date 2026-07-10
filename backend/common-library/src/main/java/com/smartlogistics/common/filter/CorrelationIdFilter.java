package com.smartlogistics.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter implements Filter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest httpServletRequest) {
            String correlationId = httpServletRequest.getHeader(CORRELATION_ID_HEADER);
            String requestId = httpServletRequest.getHeader(REQUEST_ID_HEADER);

            if (correlationId == null || correlationId.trim().isEmpty()) {
                correlationId = UUID.randomUUID().toString();
            }
            if (requestId == null || requestId.trim().isEmpty()) {
                requestId = UUID.randomUUID().toString();
            }

            MDC.put("correlationId", correlationId);
            MDC.put("requestId", requestId);

            if (response instanceof HttpServletResponse httpServletResponse) {
                httpServletResponse.setHeader(CORRELATION_ID_HEADER, correlationId);
                httpServletResponse.setHeader(REQUEST_ID_HEADER, requestId);
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("correlationId");
            MDC.remove("requestId");
        }
    }
}
