package com.mediconnect.medical.repository;

import com.mediconnect.medical.entity.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    List<MedicalHistory> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<MedicalHistory> findByDoctorIdAndPatientId(Long doctorId, Long patientId);
    List<MedicalHistory> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);
}
