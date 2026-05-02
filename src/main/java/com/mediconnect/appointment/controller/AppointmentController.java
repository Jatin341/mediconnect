package com.mediconnect.appointment.controller;

import com.mediconnect.appointment.dto.AppointmentBookRequest;
import com.mediconnect.appointment.dto.AppointmentMapper;
import com.mediconnect.appointment.dto.AppointmentResponseDTO;
import com.mediconnect.appointment.dto.SlotCreateRequest;
import com.mediconnect.appointment.entity.Appointment;
import com.mediconnect.appointment.entity.TimeSlot;
import com.mediconnect.appointment.enums.AppointmentStatus;
import com.mediconnect.appointment.repository.AppointmentRepository; // ✅ ADD
import com.mediconnect.appointment.service.AppointmentService;
import com.mediconnect.appointment.service.SlotAvailabilityService;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat; // ✅ ADD
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate; // ✅ ADD
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final SlotAvailabilityService slotAvailabilityService;
    private final AppointmentRepository appointmentRepository; // ✅ ADD

    @GetMapping("/slots/{doctorId}")
    public ResponseEntity<List<TimeSlot>> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(slotAvailabilityService.getAvailableSlots(doctorId, date));
    }

    @PostMapping("/slots")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<TimeSlot> createSlot(@RequestBody SlotCreateRequest request) {
        return ResponseEntity.ok(appointmentService.createSlot(request));
    }

    @PostMapping("/book")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponseDTO> bookAppointment(
            @RequestBody AppointmentBookRequest request) {
        Appointment appointment = appointmentService.bookAppointment(
                request.getPatientId(),
                request.getDoctorId(),
                request.getSlotId(),
                request.getSymptoms()
        );
        return ResponseEntity.ok(AppointmentMapper.toDTO(appointment));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<AppointmentResponseDTO>> getPatientAppointments(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                appointmentService.getPatientAppointments(patientId)
                        .stream()
                        .map(AppointmentMapper::toDTO)
                        .toList()
        );
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<AppointmentResponseDTO>> getDoctorAppointments(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(
                appointmentService.getDoctorAppointments(doctorId)
                        .stream()
                        .map(AppointmentMapper::toDTO)
                        .toList()
        );
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<AppointmentResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(
                AppointmentMapper.toDTO(appointmentService.updateStatus(id, status))
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                AppointmentMapper.toDTO(
                        appointmentService.updateStatus(id, AppointmentStatus.CANCELLED))
        );
    }

    // Pending appointments wale patients — doctor ke liye
    @GetMapping("/pending-patients")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentResponseDTO>> getPendingPatients() {
        return ResponseEntity.ok(
                appointmentService.getPendingAppointments()
                        .stream()
                        .map(AppointmentMapper::toDTO)
                        .toList()
        );
    }

    // ✅ NEW: Reschedule Appointment
    @PatchMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ResponseEntity<AppointmentResponseDTO> rescheduleAppointment(
            @PathVariable Long id,
            @RequestParam Long newSlotId) {

        return ResponseEntity.ok(
                AppointmentMapper.toDTO(
                        appointmentService.rescheduleAppointment(id, newSlotId))
        );
    }

    // ✅ NEW: Calendar View
    @GetMapping("/doctor/{doctorId}/calendar")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Map<String, List<AppointmentResponseDTO>>> getDoctorCalendar(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Appointment> appointments = appointmentRepository
                .findDoctorAppointmentsInRange(
                        doctorId,
                        startDate.atStartOfDay(),
                        endDate.atTime(23, 59, 59));

        Map<String, List<AppointmentResponseDTO>> calendar = appointments.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getAppointmentTime().toLocalDate().toString(),
                        Collectors.mapping(
                                AppointmentMapper::toDTO,
                                Collectors.toList()
                        )
                ));

        return ResponseEntity.ok(calendar);
    }
}
