package com.mediconnect.appointment.repository;

import com.mediconnect.appointment.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByDoctorIdAndDateAndIsBookedFalse(Long doctorId, LocalDate date);
    List<TimeSlot> findByDoctorIdAndDate(Long doctorId, LocalDate date);
}
