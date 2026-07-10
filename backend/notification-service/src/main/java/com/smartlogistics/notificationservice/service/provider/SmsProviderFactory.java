package com.smartlogistics.notificationservice.service.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsProviderFactory {

    private final MockSmsProvider mockSmsProvider;

    public SmsProvider getProvider(String type) {
        // Under a production load, config variables can determine Twilio, Plivo, etc.
        return mockSmsProvider;
    }
}
