package com.smartlogistics.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class FeignInterceptorConfig {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                String correlationId = MDC.get(CORRELATION_ID_HEADER);
                if (correlationId == null) {
                    correlationId = UUID.randomUUID().toString();
                }
                template.header(CORRELATION_ID_HEADER, correlationId);
            }
        };
    }
}
