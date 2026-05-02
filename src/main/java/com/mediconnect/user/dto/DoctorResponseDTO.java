package com.mediconnect.user.dto;

import com.mediconnect.user.enums.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DoctorResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Specialization specialization;
    private Integer experienceYears;
    private String qualification;
    private String hospitalName;
    private String hospitalAddress;
    private Double consultationFee;
    private String bio;
    private Double averageRating;
    private Integer totalReviews;
    private boolean availableForOnlineConsult;
    private LocalDateTime createdAt;
}
