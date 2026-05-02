package com.mediconnect.appointment.service;

import com.mediconnect.appointment.dto.SlotCreateRequest;
import com.mediconnect.appointment.entity.Appointment;
import com.mediconnect.appointment.entity.TimeSlot;
import com.mediconnect.appointment.enums.AppointmentStatus;
import com.mediconnect.appointment.repository.AppointmentRepository;
import com.mediconnect.appointment.repository.TimeSlotRepository;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.notification.service.EmailService;
import com.mediconnect.notification.service.NotificationService;
import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.entity.Patient;
import com.mediconnect.user.repository.DoctorRepository;
import com.mediconnect.user.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final SlotAvailabilityService slotAvailabilityService;
    private final EmailService emailService;
    private final NotificationService notificationService;

    // ✅ BOOK APPOINTMENT
    @Transactional
    public Appointment bookAppointment(Long patientId, Long doctorId, Long slotId, String symptoms) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        TimeSlot slot = timeSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        if (slot.isBooked()) {
            throw new RuntimeException("Slot is already booked");
        }

        // mark slot booked
        slot.setBooked(true);
        timeSlotRepository.save(slot);

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentTime(slot.getDate().atTime(slot.getStartTime()))
                .status(AppointmentStatus.CONFIRMED)
                .symptoms(symptoms)
                .meetingRoomId(UUID.randomUUID().toString())
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        // ✅ SEND CONFIRMATION EMAIL
        try {
            emailService.sendAppointmentConfirmation(
                    patient.getEmail(),
                    patient.getFullName(),
                    doctor.getFullName(),
                    saved.getAppointmentTime().toString(),
                    saved.getMeetingRoomId()
            );
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }

        slotAvailabilityService.evictSlotCache(doctorId, slot.getDate());

        notificationService.sendNotification(doctor.getId(),
                "🆕 New appointment by " + patient.getFullName(), "APPOINTMENT");
        notificationService.sendNotification(patient.getId(),
                "✅ Confirmed with Dr. " + doctor.getFullName(), "APPOINTMENT");

        return saved;
    }

    // ✅ GET PATIENT APPOINTMENTS
    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentTimeDesc(patientId);
    }

    // ✅ GET PENDING (CONFIRMED) APPOINTMENTS
    public List<Appointment> getPendingAppointments() {
        return appointmentRepository.findByStatusOrderByAppointmentTimeAsc(
                AppointmentStatus.CONFIRMED);
    }

    // ✅ GET DOCTOR APPOINTMENTS
    public List<Appointment> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentTimeDesc(doctorId);
    }

    // ✅ UPDATE STATUS + CANCEL EMAIL
    @Transactional
    public Appointment updateStatus(Long appointmentId, AppointmentStatus status) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setStatus(status);
        Appointment saved = appointmentRepository.save(appointment);

        // ✅ EMAIL ON CANCELLATION
        if (status == AppointmentStatus.CANCELLED) {
            try {
                emailService.sendCancellationEmail(
                        saved.getPatient().getEmail(),
                        saved.getPatient().getFullName(),
                        saved.getDoctor().getFullName(),
                        saved.getAppointmentTime().toString()
                );
            } catch (Exception e) {
                System.out.println("Cancel email failed: " + e.getMessage());
            }
        }

        if (status == AppointmentStatus.CANCELLED) {
            notificationService.sendNotification(saved.getPatient().getId(),
                    "❌ Appointment cancelled with Dr. " + saved.getDoctor().getFullName(), "CANCELLED");
        }

        return saved;
    }

    // ✅ CREATE SLOT
    @Transactional
    public TimeSlot createSlot(SlotCreateRequest request) {

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        TimeSlot slot = TimeSlot.builder()
                .doctor(doctor)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .isBooked(false)
                .build();

        return timeSlotRepository.save(slot);
    }
    @Transactional
    public Appointment rescheduleAppointment(Long appointmentId, Long newSlotId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        TimeSlot newSlot = timeSlotRepository.findById(newSlotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        if (newSlot.isBooked()) throw new RuntimeException("Slot already booked");

        newSlot.setBooked(true);
        timeSlotRepository.save(newSlot);

        appointment.setAppointmentTime(newSlot.getDate().atTime(newSlot.getStartTime()));
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        slotAvailabilityService.evictSlotCache(appointment.getDoctor().getId(), newSlot.getDate());

        notificationService.sendNotification(appointment.getPatient().getId(),
                "📅 Appointment rescheduled with Dr. " + appointment.getDoctor().getFullName(), "RESCHEDULE");

        return appointmentRepository.save(appointment);
    }
}