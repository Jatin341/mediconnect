package com.mediconnect.admin;

import com.mediconnect.appointment.dto.AppointmentMapper;
import com.mediconnect.appointment.dto.AppointmentResponseDTO;
import com.mediconnect.appointment.repository.AppointmentRepository;
import com.mediconnect.user.dto.DoctorResponseDTO;
import com.mediconnect.user.dto.PatientResponseDTO;
import com.mediconnect.user.dto.UserMapper;
import com.mediconnect.user.repository.DoctorRepository;
import com.mediconnect.user.repository.PatientRepository;
import com.mediconnect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    // Dashboard stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(Map.of(
                "totalDoctors", doctorRepository.count(),
                "totalPatients", patientRepository.count(),
                "totalUsers", userRepository.count(),
                "totalAppointments", appointmentRepository.count()
        ));
    }

    // All doctors
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        return ResponseEntity.ok(
                doctorRepository.findAll().stream()
                        .map(UserMapper::toDoctor).toList()
        );
    }

    // All patients
    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(
                patientRepository.findAll().stream()
                        .map(UserMapper::toPatient).toList()
        );
    }

    // All appointments
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        return ResponseEntity.ok(
                appointmentRepository.findAllWithDetails()
                        .stream()
                        .map(AppointmentMapper::toDTO)
                        .toList()
        );
    }

    // Doctor toggle availability
    @PatchMapping("/doctors/{id}/toggle")
    public ResponseEntity<DoctorResponseDTO> toggleDoctor(@PathVariable Long id) {
        var doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setAvailableForOnlineConsult(!doctor.isAvailableForOnlineConsult());
        return ResponseEntity.ok(UserMapper.toDoctor(doctorRepository.save(doctor)));
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}