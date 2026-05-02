package com.mediconnect.medical.dto;

import com.mediconnect.user.dto.DoctorResponseDTO;
import com.mediconnect.user.dto.PatientResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MedicalHistoryResponseDTO {
    private Long id;
    private PatientResponseDTO patient;
    private DoctorResponseDTO doctor;
    private Long appointmentId;
    private String diagnosis;
    private String doctorNotes;
    private String symptoms;
    private String treatmentPlan;
    private LocalDateTime createdAt;
}
