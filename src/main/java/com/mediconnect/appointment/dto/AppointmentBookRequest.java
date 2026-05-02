package com.mediconnect.appointment.dto;

import lombok.Data;

@Data
public class AppointmentBookRequest {
    private Long patientId;
    private Long doctorId;
    private Long slotId;
    private String symptoms;
}