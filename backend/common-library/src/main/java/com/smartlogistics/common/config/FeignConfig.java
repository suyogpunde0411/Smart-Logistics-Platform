package com.smartlogistics.common.config;

import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public Retryer feignRetryer() {
        // Backoff: 100ms, Max Wait: 1s, Max Attempts: 3
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1), 3);
    }
}
