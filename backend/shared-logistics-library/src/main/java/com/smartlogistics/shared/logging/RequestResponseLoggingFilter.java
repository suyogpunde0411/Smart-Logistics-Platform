package com.smartlogistics.shared.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RequestResponseLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {
            String method = httpRequest.getMethod();
            String uri = httpRequest.getRequestURI();
            String correlationId = MDC.get("correlationId");

            log.info("[Request Started] Method: {}, URI: {}, Correlation ID: {}", method, uri, correlationId);
            long startTime = System.currentTimeMillis();

            try {
                chain.doFilter(request, response);
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                int status = httpResponse.getStatus();
                log.info("[Request Finished] Method: {}, URI: {}, Status: {}, Duration: {}ms, Correlation ID: {}",
                        method, uri, status, duration, correlationId);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
