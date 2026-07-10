package com.smartlogistics.matchingservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI matchingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Logistics Matching Service API")
                        .version("1.0.0")
                        .description("Rule-based and AI-ready shipment/truck matching APIs"));
    }
}
