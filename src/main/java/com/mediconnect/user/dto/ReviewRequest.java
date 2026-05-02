package com.mediconnect.user.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long doctorId;
    private Long patientId;
    private Double rating;  // 1.0 to 5.0
    private String comment;
}