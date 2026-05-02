package com.mediconnect.appointment.entity;

import com.mediconnect.appointment.enums.AppointmentStatus;
import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.entity.Patient;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private LocalDateTime appointmentTime;

    private Integer durationMinutes = 30;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    private String symptoms;

    private String notes;

    private String meetingRoomId;   // for WebRTC

    @CreatedDate
    private LocalDateTime bookedAt;

    private LocalDateTime completedAt;
}