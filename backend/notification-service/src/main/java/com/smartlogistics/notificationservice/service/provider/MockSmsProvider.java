package com.smartlogistics.notificationservice.service.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MockSmsProvider implements SmsProvider {
    @Override
    public void sendSms(String phoneNumber, String message) {
        log.info("[SMS DISPATCH] Recipient: {}, Content: {}", phoneNumber, message);
    }
}
