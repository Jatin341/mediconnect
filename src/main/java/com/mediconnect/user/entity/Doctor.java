package com.mediconnect.user.entity;

import com.mediconnect.user.enums.Specialization;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "doctors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
@PrimaryKeyJoinColumn(name = "user_id")
public class Doctor extends User {

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private Integer experienceYears;

    private String qualification;   // MBBS, MD, etc.

    private String hospitalName;

    private String hospitalAddress;

    private Double consultationFee;

    private String bio;

    private String profileImageUrl;

    @Column(columnDefinition = "DECIMAL(3,2)")
    private Double averageRating = 0.0;

    private Integer totalReviews = 0;

    private boolean availableForOnlineConsult = true;
}
