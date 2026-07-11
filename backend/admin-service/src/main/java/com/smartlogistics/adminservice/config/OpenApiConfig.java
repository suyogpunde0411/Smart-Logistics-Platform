package com.smartlogistics.adminservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Admin Service API",
                version = "1.0",
                description = "Documentation for Admin Service API in Smart Logistics Optimization Platform",
                contact = @Contact(
                        name = "Smart Logistics Team",
                        email = "admin@smartlogistics.com"
                )
        )
)
public class OpenApiConfig {
}
