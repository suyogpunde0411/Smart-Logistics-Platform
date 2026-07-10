package com.smartlogistics.notificationservice.service.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MockPushProvider implements PushProvider {
    @Override
    public void sendPush(String deviceToken, String title, String body) {
        log.info("[PUSH DISPATCH] Token: {}, Title: {}, Body: {}", deviceToken, title, body);
    }
}
