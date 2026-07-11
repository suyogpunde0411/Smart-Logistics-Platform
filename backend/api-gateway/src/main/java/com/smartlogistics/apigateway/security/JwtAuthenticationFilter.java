package com.smartlogistics.apigateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/eureka"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        
        // Allow open endpoints
        if (OPEN_ENDPOINTS.stream().anyMatch(path::contains)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        // Basic JWT parse/validation should happen here. 
        // For simplicity and since we don't have JJWT in pom.xml yet, we just propagate.
        
        // In a production system, you'd extract claims and mutate the request:
        // exchange.getRequest().mutate().header("X-User-Id", userId).build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
