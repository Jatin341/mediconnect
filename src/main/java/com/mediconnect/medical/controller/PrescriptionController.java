package com.mediconnect.medical.controller;

import com.mediconnect.medical.dto.PrescriptionRequest;
import com.mediconnect.medical.entity.MedicalHistory;
import com.mediconnect.medical.entity.Prescription;
import com.mediconnect.medical.repository.MedicalHistoryRepository;
import com.mediconnect.medical.repository.PrescriptionRepository;
import com.mediconnect.medical.service.PdfGeneratorService;
import com.mediconnect.notification.service.EmailService;
import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.entity.Patient;
import com.mediconnect.user.repository.DoctorRepository;
import com.mediconnect.user.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PdfGeneratorService pdfGeneratorService;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final EmailService emailService;

    // Generate + Download PDF
    @PostMapping("/generate")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<byte[]> generatePrescription(
            @RequestBody PrescriptionRequest request) {

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Generate PDF
        byte[] pdfBytes = pdfGeneratorService.generatePrescriptionPdf(
                request, doctor, patient);

        // Save record
        if (request.getMedicalHistoryId() != null) {
            MedicalHistory history = medicalHistoryRepository
                    .findById(request.getMedicalHistoryId()).orElse(null);
            if (history != null) {
                Prescription prescription = Prescription.builder()
                        .medicalHistory(history)
                        .fileName("prescription_" + patient.getFullName()
                                .replace(" ", "_") + ".pdf")
                        .fileType("application/pdf")
                        .fileUrl("generated")
                        .build();
                prescriptionRepository.save(prescription);
            }
        }

        // Email send karo patient ko
        try {
            emailService.sendPrescriptionEmail(
                    patient.getEmail(),
                    patient.getFullName(),
                    doctor.getFullName(),
                    pdfBytes
            );
        } catch (Exception e) {
            System.out.println("Prescription email failed: " + e.getMessage());
        }

        // Return PDF as download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment",
                "prescription_" + patient.getFullName().replace(" ", "_") + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    @GetMapping("/medical/{medicalHistoryId}")
    public ResponseEntity<?> getPrescriptions(@PathVariable Long medicalHistoryId) {
        return ResponseEntity.ok(
                prescriptionRepository.findByMedicalHistoryId(medicalHistoryId));
    }
}