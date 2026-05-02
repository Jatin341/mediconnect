package com.mediconnect.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DoctorStatusService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ONLINE_KEY = "doctor:online:";
    private static final Duration TTL = Duration.ofMinutes(5);

    public void setOnline(Long doctorId) {
        redisTemplate.opsForValue().set(ONLINE_KEY + doctorId, "true", TTL);
    }

    public void setOffline(Long doctorId) {
        redisTemplate.delete(ONLINE_KEY + doctorId);
    }

    public boolean isOnline(Long doctorId) {
        return Boolean.TRUE.equals(
                redisTemplate.hasKey(ONLINE_KEY + doctorId));
    }

    // Heartbeat — doctor har 3 min mein ping karega
    public void heartbeat(Long doctorId) {
        redisTemplate.opsForValue().set(ONLINE_KEY + doctorId, "true", TTL);
    }
}
