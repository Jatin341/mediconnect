package com.mediconnect.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String OTP_PREFIX = "otp:";
    private static final Duration OTP_TTL = Duration.ofMinutes(5);

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        redisTemplate.opsForValue().set(OTP_PREFIX + email, otp, OTP_TTL);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        Object stored = redisTemplate.opsForValue().get(OTP_PREFIX + email);
        if (stored != null && stored.toString().equals(otp)) {
            redisTemplate.delete(OTP_PREFIX + email);
            return true;
        }
        return false;
    }

    public boolean otpExists(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(OTP_PREFIX + email));
    }
}