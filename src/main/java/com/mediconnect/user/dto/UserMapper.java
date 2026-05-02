package com.mediconnect.user.dto;

import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.entity.Patient;

public class UserMapper {

    public static DoctorResponseDTO toDoctor(Doctor d) {
        return DoctorResponseDTO.builder()
                .id(d.getId())
                .fullName(d.getFullName())
                .email(d.getEmail())
                .phone(d.getPhone())
                .specialization(d.getSpecialization())
                .experienceYears(d.getExperienceYears())
                .qualification(d.getQualification())
                .hospitalName(d.getHospitalName())
                .hospitalAddress(d.getHospitalAddress())
                .consultationFee(d.getConsultationFee())
                .bio(d.getBio())
                .averageRating(d.getAverageRating())
                .totalReviews(d.getTotalReviews())
                .availableForOnlineConsult(d.isAvailableForOnlineConsult())
                .createdAt(d.getCreatedAt())
                .build();
    }

    public static PatientResponseDTO toPatient(Patient p) {
        return PatientResponseDTO.builder()
                .id(p.getId())
                .fullName(p.getFullName())
                .email(p.getEmail())
                .phone(p.getPhone())
                .dateOfBirth(p.getDateOfBirth())
                .gender(p.getGender())
                .bloodGroup(p.getBloodGroup())
                .heightCm(p.getHeightCm())
                .weightKg(p.getWeightKg())
                .allergies(p.getAllergies())
                .chronicConditions(p.getChronicConditions())
                .emergencyContactName(p.getEmergencyContactName())
                .emergencyContactPhone(p.getEmergencyContactPhone())
                .address(p.getAddress())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
