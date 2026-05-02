package com.mediconnect.admin;

import com.mediconnect.appointment.enums.AppointmentStatus;
import com.mediconnect.appointment.repository.AppointmentRepository;
import com.mediconnect.user.repository.DoctorRepository;
import com.mediconnect.user.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    // Doctor ka personal analytics
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getDoctorAnalytics(
            @PathVariable Long doctorId) {

        long totalAppointments = appointmentRepository
                .findByDoctorIdOrderByAppointmentTimeDesc(doctorId).size();

        long completedAppointments = appointmentRepository
                .findByDoctorIdAndStatus(doctorId, AppointmentStatus.COMPLETED).size();

        long confirmedAppointments = appointmentRepository
                .findByDoctorIdAndStatus(doctorId, AppointmentStatus.CONFIRMED).size();

        long cancelledAppointments = appointmentRepository
                .findByDoctorIdAndStatus(doctorId, AppointmentStatus.CANCELLED).size();

        // Earnings calculate (consultation fee * completed appointments)
        double earnings = doctorRepository.findById(doctorId)
                .map(d -> (d.getConsultationFee() != null ? d.getConsultationFee() : 0.0)
                        * completedAppointments)
                .orElse(0.0);

        return ResponseEntity.ok(Map.of(
                "totalAppointments", totalAppointments,
                "completedAppointments", completedAppointments,
                "confirmedAppointments", confirmedAppointments,
                "cancelledAppointments", cancelledAppointments,
                "estimatedEarnings", earnings,
                "completionRate", totalAppointments > 0
                        ? Math.round((completedAppointments * 100.0) / totalAppointments) : 0
        ));
    }

    // Platform-wide analytics (admin)
    @GetMapping("/platform")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPlatformAnalytics() {
        return ResponseEntity.ok(Map.of(
                "totalDoctors", doctorRepository.count(),
                "totalPatients", patientRepository.count(),
                "totalAppointments", appointmentRepository.count(),
                "completedAppointments", appointmentRepository
                        .findByStatusOrderByAppointmentTimeAsc(AppointmentStatus.COMPLETED).size(),
                "confirmedAppointments", appointmentRepository
                        .findByStatusOrderByAppointmentTimeAsc(AppointmentStatus.CONFIRMED).size()
        ));
    }
}