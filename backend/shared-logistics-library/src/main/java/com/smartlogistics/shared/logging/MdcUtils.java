package com.smartlogistics.shared.logging;

import org.slf4j.MDC;

public class MdcUtils {
    public static void put(String key, String value) {
        if (key != null && value != null) {
            MDC.put(key, value);
        }
    }
    public static String get(String key) {
        if (key == null) return null;
        return MDC.get(key);
    }
    public static void remove(String key) {
        if (key != null) {
            MDC.remove(key);
        }
    }
    public static void clear() {
        MDC.clear();
    }
}
