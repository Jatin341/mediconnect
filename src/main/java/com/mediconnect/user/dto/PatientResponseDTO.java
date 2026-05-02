package com.mediconnect.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatientResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private Double heightCm;
    private Double weightKg;
    private String allergies;
    private String chronicConditions;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String address;
    private LocalDateTime createdAt;
}
