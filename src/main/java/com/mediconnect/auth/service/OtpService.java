package com.mediconnect.auth.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, new OtpEntry(otp,
                System.currentTimeMillis() + 5 * 60 * 1000));
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        OtpEntry entry = otpStore.get(email);
        if (entry == null) return false;
        if (System.currentTimeMillis() > entry.expiry()) return false;
        if (!entry.otp().equals(otp)) return false;
        otpStore.remove(email);
        return true;
    }

    public boolean otpExists(String email) {
        OtpEntry entry = otpStore.get(email);
        if (entry == null) return false;
        return System.currentTimeMillis() < entry.expiry();
    }

    private record OtpEntry(String otp, long expiry) {}
}