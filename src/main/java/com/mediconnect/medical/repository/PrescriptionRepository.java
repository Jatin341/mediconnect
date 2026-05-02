package com.mediconnect.medical.repository;

import com.mediconnect.medical.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    // Custom method to fetch prescriptions by medicalHistoryId
    List<Prescription> findByMedicalHistoryId(Long medicalHistoryId);
}