package com.smartlogistics.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcLoggingFilter implements Filter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        try {
            if (request instanceof HttpServletRequest httpRequest) {
                String correlationId = httpRequest.getHeader(CORRELATION_ID_HEADER);
                if (correlationId == null || correlationId.isEmpty()) {
                    correlationId = UUID.randomUUID().toString();
                }
                MDC.put(CORRELATION_ID_HEADER, correlationId);
            }
            chain.doFilter(request, response);
        } finally {
            MDC.remove(CORRELATION_ID_HEADER);
        }
    }
}
