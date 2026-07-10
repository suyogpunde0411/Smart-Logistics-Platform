package com.smartlogistics.matchingservice.config;

import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.stream.Collectors;

@Configuration
public class MatchingFeignConfig {

    @Bean
    public RequestInterceptor securityHeaderInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() != null) {
                requestTemplate.header("X-User-Id", authentication.getPrincipal().toString());
                String roles = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(role -> role.replace("ROLE_", ""))
                        .collect(Collectors.joining(","));
                requestTemplate.header("X-User-Roles", roles);
            }
            String correlationId = MDC.get("correlationId");
            if (correlationId != null) {
                requestTemplate.header("X-Correlation-Id", correlationId);
            }
        };
    }
}
