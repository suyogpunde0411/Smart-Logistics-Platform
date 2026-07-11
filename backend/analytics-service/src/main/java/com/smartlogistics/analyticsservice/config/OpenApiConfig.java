package com.smartlogistics.analyticsservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI analyticsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Logistics Analytics Service API")
                        .version("1.0.0")
                        .description("Analytics dashboard metrics and strategic business intelligence reporting endpoints."));
    }
}
