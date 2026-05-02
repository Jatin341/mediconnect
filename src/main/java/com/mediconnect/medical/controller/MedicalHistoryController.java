package com.mediconnect.medical.controller;

import com.mediconnect.medical.dto.MedicalHistoryMapper;
import com.mediconnect.medical.dto.MedicalHistoryRequest;
import com.mediconnect.medical.dto.MedicalHistoryResponseDTO;
import com.mediconnect.medical.service.MedicalHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical")
@RequiredArgsConstructor
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN', 'PATIENT')")
    public ResponseEntity<List<MedicalHistoryResponseDTO>> getPatientHistory(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                medicalHistoryService.getPatientHistory(patientId)
                        .stream()
                        .map(MedicalHistoryMapper::toDTO)
                        .toList()
        );
    }

    @GetMapping("/doctor/{doctorId}/patient/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<MedicalHistoryResponseDTO>> getDoctorPatientHistory(
            @PathVariable Long doctorId,
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                medicalHistoryService.getDoctorPatientHistory(doctorId, patientId)
                        .stream()
                        .map(MedicalHistoryMapper::toDTO)
                        .toList()
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalHistoryResponseDTO> addMedicalHistory(
            @RequestBody MedicalHistoryRequest request) {
        return ResponseEntity.ok(
                MedicalHistoryMapper.toDTO(
                        medicalHistoryService.addMedicalHistory(request))
        );
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<MedicalHistoryResponseDTO>> getDoctorHistory(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(
                medicalHistoryService.getDoctorHistory(doctorId)
                        .stream()
                        .map(MedicalHistoryMapper::toDTO)
                        .toList()
        );
    }
}