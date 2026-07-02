package com.smartlogistics.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void blacklistToken(String token, long expirationMs) {
        redisTemplate.opsForValue().set("blacklist:" + token, true, Duration.ofMillis(expirationMs));
    }

    public boolean isTokenBlacklisted(String token) {
        Boolean isBlacklisted = (Boolean) redisTemplate.opsForValue().get("blacklist:" + token);
        return isBlacklisted != null && isBlacklisted;
    }

    public void saveOtp(String identifier, String otp, long expirationMs) {
        redisTemplate.opsForValue().set("otp:" + identifier, otp, Duration.ofMillis(expirationMs));
    }

    public String getOtp(String identifier) {
        return (String) redisTemplate.opsForValue().get("otp:" + identifier);
    }

    public void deleteOtp(String identifier) {
        redisTemplate.delete("otp:" + identifier);
    }

    public void incrementLoginAttempts(String ipAddress) {
        String key = "login_attempt:" + ipAddress;
        Long attempts = redisTemplate.opsForValue().increment(key);
        if (attempts != null && attempts == 1) {
            redisTemplate.expire(key, Duration.ofMinutes(15));
        }
    }
    
    public int getLoginAttempts(String ipAddress) {
        String key = "login_attempt:" + ipAddress;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        return attempts == null ? 0 : attempts;
    }

    public void resetLoginAttempts(String ipAddress) {
        redisTemplate.delete("login_attempt:" + ipAddress);
    }
}
