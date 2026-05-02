package com.mediconnect.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
@PrimaryKeyJoinColumn(name = "user_id")
public class Patient extends User {

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
}
