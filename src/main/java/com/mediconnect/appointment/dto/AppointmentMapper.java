package com.mediconnect.appointment.dto;

import com.mediconnect.appointment.entity.Appointment;
import com.mediconnect.user.dto.UserMapper;

public class AppointmentMapper {

    public static AppointmentResponseDTO toDTO(Appointment a) {
        return AppointmentResponseDTO.builder()
                .id(a.getId())
                .patient(UserMapper.toPatient(a.getPatient()))
                .doctor(UserMapper.toDoctor(a.getDoctor()))
                .appointmentTime(a.getAppointmentTime())
                .durationMinutes(a.getDurationMinutes())
                .status(a.getStatus())
                .symptoms(a.getSymptoms())
                .notes(a.getNotes())
                .meetingRoomId(a.getMeetingRoomId())
                .bookedAt(a.getBookedAt())
                .build();
    }
}
