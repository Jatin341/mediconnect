package com.mediconnect.appointment.entity;

import com.mediconnect.user.entity.Doctor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "time_slots")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean isBooked = false;
}
