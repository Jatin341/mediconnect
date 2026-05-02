package com.mediconnect.medical.dto;

import lombok.Data;

@Data
public class PrescriptionItem {
    private String medicineName;
    private String dosage;        // e.g. "500mg"
    private String frequency;     // e.g. "Twice daily"
    private String duration;      // e.g. "7 days"
    private String instructions;  // e.g. "After meals"
}