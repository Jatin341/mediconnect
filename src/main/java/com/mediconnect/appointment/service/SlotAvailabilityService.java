package com.mediconnect.appointment.service;

import com.mediconnect.appointment.entity.TimeSlot;
import com.mediconnect.appointment.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotAvailabilityService {

    private final TimeSlotRepository timeSlotRepository;

    @Cacheable(value = "doctorSlots", key = "#doctorId + '-' + #date")
    public List<TimeSlot> getAvailableSlots(Long doctorId, LocalDate date) {
        return timeSlotRepository.findByDoctorIdAndDateAndIsBookedFalse(doctorId, date);
    }

    @CacheEvict(value = "doctorSlots", key = "#doctorId + '-' + #date")
    public void evictSlotCache(Long doctorId, LocalDate date) {
        // Cache evicted on booking
    }
}
