package com.mediconnect.user.service;

import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.user.entity.Patient;
import com.mediconnect.user.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    @Transactional
    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient patient = getPatientById(id);
        patient.setDateOfBirth(updatedPatient.getDateOfBirth());
        patient.setGender(updatedPatient.getGender());
        patient.setBloodGroup(updatedPatient.getBloodGroup());
        patient.setHeightCm(updatedPatient.getHeightCm());
        patient.setWeightKg(updatedPatient.getWeightKg());
        patient.setAllergies(updatedPatient.getAllergies());
        patient.setChronicConditions(updatedPatient.getChronicConditions());
        patient.setEmergencyContactName(updatedPatient.getEmergencyContactName());
        patient.setEmergencyContactPhone(updatedPatient.getEmergencyContactPhone());
        patient.setAddress(updatedPatient.getAddress());
        return patientRepository.save(patient);
    }
}