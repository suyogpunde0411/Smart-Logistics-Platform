package com.smartlogistics.truckservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.smartlogistics.truckservice", "com.smartlogistics.common", "com.smartlogistics.shared"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.smartlogistics.common.client"})
public class TruckServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TruckServiceApplication.class, args);
    }
}
