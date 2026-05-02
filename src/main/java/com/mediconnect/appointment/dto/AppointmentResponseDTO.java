package com.mediconnect.appointment.dto;

import com.mediconnect.appointment.enums.AppointmentStatus;
import com.mediconnect.user.dto.DoctorResponseDTO;
import com.mediconnect.user.dto.PatientResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AppointmentResponseDTO {
    private Long id;
    private PatientResponseDTO patient;
    private DoctorResponseDTO doctor;
    private LocalDateTime appointmentTime;
    private Integer durationMinutes;
    private AppointmentStatus status;
    private String symptoms;
    private String notes;
    private String meetingRoomId;
    private LocalDateTime bookedAt;
}