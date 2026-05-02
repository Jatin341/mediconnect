package com.mediconnect.medical.dto;

import lombok.Data;
import java.util.List;

@Data
public class PrescriptionRequest {
    private Long medicalHistoryId;
    private Long doctorId;
    private Long patientId;
    private List<PrescriptionItem> medicines;
    private String notes;
}