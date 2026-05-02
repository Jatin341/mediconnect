package com.mediconnect.medical.dto;

import com.mediconnect.medical.entity.MedicalHistory;
import com.mediconnect.user.dto.UserMapper;

public class MedicalHistoryMapper {

    public static MedicalHistoryResponseDTO toDTO(MedicalHistory m) {
        return MedicalHistoryResponseDTO.builder()
                .id(m.getId())
                .patient(UserMapper.toPatient(m.getPatient()))
                .doctor(UserMapper.toDoctor(m.getDoctor()))
                .appointmentId(m.getAppointment() != null ? m.getAppointment().getId() : null)
                .diagnosis(m.getDiagnosis())
                .doctorNotes(m.getDoctorNotes())
                .symptoms(m.getSymptoms())
                .treatmentPlan(m.getTreatmentPlan())
                .createdAt(m.getCreatedAt())
                .build();
    }
}