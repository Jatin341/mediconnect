package com.mediconnect.appointment.service;

import com.mediconnect.appointment.entity.TimeSlot;
import com.mediconnect.appointment.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotAvailabilityService {

    private final TimeSlotRepository timeSlotRepository;

    public List<TimeSlot> getAvailableSlots(Long doctorId, LocalDate date) {
        return timeSlotRepository
                .findByDoctorIdAndDateAndIsBookedFalse(doctorId, date);
    }

    public void evictSlotCache(Long doctorId, LocalDate date) {
        // Cache eviction — no-op without Redis
    }
}