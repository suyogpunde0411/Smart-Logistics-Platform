package com.smartlogistics.userservice.config;

import com.smartlogistics.userservice.util.UserContext;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignClientConfig {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authHeader = request.getHeader(AUTHORIZATION_HEADER);
                if (authHeader != null && !authHeader.trim().isEmpty()) {
                    requestTemplate.header(AUTHORIZATION_HEADER, authHeader);
                }
            }

            if (UserContext.getRequestId() != null) {
                requestTemplate.header("X-Request-Id", UserContext.getRequestId());
            }
            if (UserContext.getCorrelationId() != null) {
                requestTemplate.header("X-Correlation-Id", UserContext.getCorrelationId());
            }
            if (UserContext.getUserId() != null) {
                requestTemplate.header("X-User-Id", UserContext.getUserId().toString());
            }
        };
    }
}
