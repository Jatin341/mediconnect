package com.mediconnect.user.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class DoctorStatusService {

    private final Map<Long, Long> onlineDoctors = new ConcurrentHashMap<>();
    private static final long TTL_MS = 5 * 60 * 1000; // 5 minutes

    public void setOnline(Long doctorId) {
        onlineDoctors.put(doctorId, System.currentTimeMillis());
    }

    public void setOffline(Long doctorId) {
        onlineDoctors.remove(doctorId);
    }

    public boolean isOnline(Long doctorId) {
        Long lastSeen = onlineDoctors.get(doctorId);
        if (lastSeen == null) return false;
        return System.currentTimeMillis() - lastSeen < TTL_MS;
    }

    public void heartbeat(Long doctorId) {
        onlineDoctors.put(doctorId, System.currentTimeMillis());
    }
}