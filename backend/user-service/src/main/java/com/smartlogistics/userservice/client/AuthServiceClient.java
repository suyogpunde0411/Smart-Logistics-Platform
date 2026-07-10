package com.smartlogistics.userservice.client;

import com.smartlogistics.userservice.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", path = "/api/v1/auth")
public interface AuthServiceClient {

    @GetMapping("/me")
    ApiResponse<Object> getCurrentUser(@RequestHeader("Authorization") String token);
}
