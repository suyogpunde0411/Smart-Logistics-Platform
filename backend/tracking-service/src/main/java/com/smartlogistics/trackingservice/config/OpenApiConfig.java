package com.smartlogistics.trackingservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI trackingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Logistics Tracking Service API")
                        .version("1.0.0")
                        .description("Real-time GPS Tracking, checkpoints, fuel logging, and Trip status lifecycle management APIs"));
    }
}
