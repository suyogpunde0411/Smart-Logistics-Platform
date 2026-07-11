package com.smartlogistics.reviewservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reviewOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Logistics Review & Rating Service API")
                        .version("1.0.0")
                        .description("Review & Rating management APIs (Driver, Business, Fleet Owner reviews), Reputation Trust Scores calculations, and Dispute moderator audits"));
    }
}
