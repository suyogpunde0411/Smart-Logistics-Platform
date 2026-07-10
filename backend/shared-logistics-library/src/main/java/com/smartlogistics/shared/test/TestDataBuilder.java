package com.smartlogistics.shared.test;

import java.util.UUID;

public class TestDataBuilder {
    public static String randomString(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, Math.min(length, 32));
    }
}
