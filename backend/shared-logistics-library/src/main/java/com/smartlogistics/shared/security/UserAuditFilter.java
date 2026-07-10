package com.smartlogistics.shared.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class UserAuditFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpServletRequest) {
            String userIdStr = httpServletRequest.getHeader("X-User-Id");
            if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                try {
                    UUID userId = UUID.fromString(userIdStr.trim());
                    CurrentUserUtil.setUserId(userId);
                } catch (IllegalArgumentException e) {
                    // Ignore invalid format
                }
            }
        }
        try {
            chain.doFilter(request, response);
        } finally {
            CurrentUserUtil.clear();
        }
    }
}
