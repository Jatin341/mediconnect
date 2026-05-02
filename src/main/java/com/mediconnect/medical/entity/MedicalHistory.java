package com.mediconnect.medical.entity;

import com.mediconnect.appointment.entity.Appointment;
import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.entity.Patient;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "medical_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String diagnosis;

    @Column(length = 2000)
    private String doctorNotes;

    private String symptoms;

    private String treatmentPlan;

    @OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;

    @CreatedDate
    private LocalDateTime createdAt;
}