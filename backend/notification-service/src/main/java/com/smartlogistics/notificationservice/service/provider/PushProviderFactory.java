package com.smartlogistics.notificationservice.service.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PushProviderFactory {

    private final MockPushProvider mockPushProvider;

    public PushProvider getProvider(String type) {
        // Can resolve Firebase (FCM), APNS (Apple Push), etc.
        return mockPushProvider;
    }
}
