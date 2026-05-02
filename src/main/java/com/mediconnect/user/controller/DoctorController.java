package com.mediconnect.user.controller;

import com.mediconnect.user.dto.DoctorResponseDTO;
import com.mediconnect.user.dto.ReviewRequest;
import com.mediconnect.user.dto.UserMapper;
import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.enums.Specialization;
import com.mediconnect.user.service.DoctorService;
import com.mediconnect.user.service.DoctorStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorStatusService doctorStatusService;


    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        return ResponseEntity.ok(
                doctorService.getAllDoctors()
                        .stream()
                        .map(UserMapper::toDoctor)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(UserMapper.toDoctor(doctorService.getDoctorById(id)));
    }

    @GetMapping("/specialization/{spec}")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsBySpecialization(
            @PathVariable Specialization spec) {
        return ResponseEntity.ok(
                doctorService.getDoctorsBySpecialization(spec)
                        .stream()
                        .map(UserMapper::toDoctor)
                        .toList()
        );
    }

    @GetMapping("/available")
    public ResponseEntity<List<DoctorResponseDTO>> getAvailableDoctors() {
        return ResponseEntity.ok(
                doctorService.getAvailableDoctors()
                        .stream()
                        .map(UserMapper::toDoctor)
                        .toList()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorResponseDTO>> searchDoctors(
            @RequestParam String name) {
        return ResponseEntity.ok(
                doctorService.searchByName(name)
                        .stream()
                        .map(UserMapper::toDoctor)
                        .toList()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor) {
        return ResponseEntity.ok(
                UserMapper.toDoctor(doctorService.updateDoctor(id, doctor))
        );
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponseDTO> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(
                UserMapper.toDoctor(doctorService.toggleAvailability(id))
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<List<DoctorResponseDTO>> filterDoctors(
            @RequestParam(required = false) Specialization specialization,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Double maxFee,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(
                doctorService.filterDoctors(specialization, minExperience, maxFee, name)
                        .stream().map(UserMapper::toDoctor).toList()
        );
    }

    @PostMapping("/{id}/review")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<DoctorResponseDTO> addReview(
            @PathVariable Long id,
            @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(
                UserMapper.toDoctor(doctorService.addReview(id, request))
        );
    }

    @PostMapping("/{id}/heartbeat")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, String>> heartbeat(@PathVariable Long id) {
        doctorStatusService.heartbeat(id);
        return ResponseEntity.ok(Map.of("status", "online"));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, Boolean>> getDoctorStatus(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("online", doctorStatusService.isOnline(id)));
    }
}