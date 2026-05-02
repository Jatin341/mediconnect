package com.mediconnect.medical.service;

import com.mediconnect.appointment.entity.Appointment;
import com.mediconnect.appointment.repository.AppointmentRepository;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.medical.dto.MedicalHistoryRequest;
import com.mediconnect.medical.entity.MedicalHistory;
import com.mediconnect.medical.repository.MedicalHistoryRepository;
import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.entity.Patient;
import com.mediconnect.user.repository.DoctorRepository;
import com.mediconnect.user.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalHistoryService {

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public List<MedicalHistory> getPatientHistory(Long patientId) {
        return medicalHistoryRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }
    public List<MedicalHistory> getDoctorHistory(Long doctorId) {
        return medicalHistoryRepository.findByDoctorIdOrderByCreatedAtDesc(doctorId);
    }

    public List<MedicalHistory> getDoctorPatientHistory(Long doctorId, Long patientId) {
        return medicalHistoryRepository.findByDoctorIdAndPatientId(doctorId, patientId);
    }

    @Transactional
    public MedicalHistory addMedicalHistory(MedicalHistoryRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElse(null);
        }

        MedicalHistory history = MedicalHistory.builder()
                .patient(patient)
                .doctor(doctor)
                .appointment(appointment)
                .diagnosis(request.getDiagnosis())
                .doctorNotes(request.getDoctorNotes())
                .symptoms(request.getSymptoms())
                .treatmentPlan(request.getTreatmentPlan())
                .build();

        return medicalHistoryRepository.save(history);
    }
}