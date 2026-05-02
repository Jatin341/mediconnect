package com.mediconnect.appointment;

import com.mediconnect.appointment.entity.Appointment;
import com.mediconnect.appointment.entity.TimeSlot;
import com.mediconnect.appointment.enums.AppointmentStatus;
import com.mediconnect.appointment.repository.AppointmentRepository;
import com.mediconnect.appointment.repository.TimeSlotRepository;
import com.mediconnect.appointment.service.AppointmentService;
import com.mediconnect.appointment.service.SlotAvailabilityService;
import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.entity.Patient;
import com.mediconnect.user.repository.DoctorRepository;
import com.mediconnect.user.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock AppointmentRepository appointmentRepository;
    @Mock TimeSlotRepository timeSlotRepository;
    @Mock DoctorRepository doctorRepository;
    @Mock PatientRepository patientRepository;
    @Mock SlotAvailabilityService slotAvailabilityService;

    @InjectMocks AppointmentService appointmentService;

    @Test
    void bookAppointment_Success() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFullName("Test Patient");
        patient.setEmail("patient@test.com");

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFullName("Test Doctor");

        TimeSlot slot = TimeSlot.builder()
                .id(1L).doctor(doctor)
                .date(LocalDate.now())
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(10,30))
                .isBooked(false).build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(appointmentRepository.save(any())).thenAnswer(i -> {
            Appointment a = i.getArgument(0);
            a.setId(1L);
            return a;
        });

        Appointment result = appointmentService.bookAppointment(1L, 1L, 1L, "Fever");

        assertNotNull(result);
        assertEquals(AppointmentStatus.CONFIRMED, result.getStatus());
        assertNotNull(result.getMeetingRoomId());
        assertTrue(slot.isBooked());
    }

    @Test
    void bookAppointment_SlotAlreadyBooked_ThrowsException() {
        Patient patient = new Patient(); patient.setId(1L);
        Doctor doctor = new Doctor(); doctor.setId(1L);
        TimeSlot slot = new TimeSlot(); slot.setBooked(true);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(slot));

        assertThrows(RuntimeException.class,
                () -> appointmentService.bookAppointment(1L, 1L, 1L, "Fever"));
    }
}
