package com.mediconnect.notification.service;

import com.mediconnect.appointment.entity.Appointment;
import com.mediconnect.appointment.enums.AppointmentStatus;
import com.mediconnect.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;

    // Har 1 ghante mein check karo — agle 24 ghante ki appointments
    @Scheduled(fixedRate = 3600000)
    public void sendAppointmentReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24h = now.plusHours(24);

        List<Appointment> upcoming = appointmentRepository
                .findUpcomingAppointments(now, next24h);

        for (Appointment a : upcoming) {
            try {
                emailService.sendAppointmentReminder(
                        a.getPatient().getEmail(),
                        a.getPatient().getFullName(),
                        a.getDoctor().getFullName(),
                        a.getAppointmentTime().toString()
                );
            } catch (Exception e) {
                System.out.println("Reminder failed: " + e.getMessage());
            }
        }
    }
}