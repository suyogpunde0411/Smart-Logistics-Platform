package com.smartlogistics.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "auth-service", path = "/internal/auth")
public interface AuthFeignClient {

    @PostMapping("/validate")
    UserValidationResponse validateToken(@RequestHeader("Authorization") String token);

    record UserValidationResponse(
            UUID userId,
            String email,
            Set<String> roles,
            boolean active
    ) {}
}
