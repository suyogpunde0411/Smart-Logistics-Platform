package com.smartlogistics.shipmentservice.config;

import com.smartlogistics.common.client.AuthFeignClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthFeignClient authFeignClient;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/actuator/**").permitAll()
                .requestMatchers("/internal/**").permitAll()
                .requestMatchers("/api/v1/shipments/tracking/**").permitAll()
                .requestMatchers("/api/v1/shipments/**").hasAnyRole("BUSINESS_OWNER", "ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(new SecurityHeaderFilter(authFeignClient), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @RequiredArgsConstructor
    public static class SecurityHeaderFilter extends OncePerRequestFilter {

        private final AuthFeignClient authFeignClient;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            String authorization = request.getHeader("Authorization");

            if (authorization != null && authorization.startsWith("Bearer ")) {
                try {
                    AuthFeignClient.UserValidationResponse validation = authFeignClient.validateToken(authorization);
                    if (validation != null && validation.active()) {
                        setAuthentication(validation.userId().toString(), validation.roles());
                    }
                } catch (Exception ex) {
                    SecurityContextHolder.clearContext();
                    setGatewayHeaderAuthentication(request);
                }
            } else {
                setGatewayHeaderAuthentication(request);
            }

            chain.doFilter(request, response);
        }

        private void setGatewayHeaderAuthentication(HttpServletRequest request) {
            String userId = request.getHeader("X-User-Id");
            String userRoles = request.getHeader("X-User-Roles");

            if (userId != null && !userId.trim().isEmpty()) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                if (userRoles != null && !userRoles.trim().isEmpty()) {
                    authorities = Arrays.stream(userRoles.split(","))
                            .map(String::trim)
                            .map(this::normalizeRole)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                }
                Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        private void setAuthentication(String principal, Collection<String> roles) {
            List<GrantedAuthority> authorities = roles == null ? List.of() : roles.stream()
                    .map(this::normalizeRole)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        private String normalizeRole(String role) {
            return role.startsWith("ROLE_") ? role : "ROLE_" + role;
        }
    }
}
