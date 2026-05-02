package com.mediconnect.medical.dto;

import lombok.Data;

@Data
public class MedicalHistoryRequest {
    private Long patientId;
    private Long doctorId;
    private Long appointmentId;
    private String diagnosis;
    private String doctorNotes;
    private String symptoms;
    private String treatmentPlan;
}
