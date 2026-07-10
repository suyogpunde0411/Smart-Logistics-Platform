package com.smartlogistics.auth.client;

import com.smartlogistics.auth.dto.UserRegisteredEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserClient {
    
    @PostMapping("/api/v1/users/sync")
    void syncRegisteredUser(@RequestBody UserRegisteredEvent event);
}
