package com.mediconnect.user.repository;

import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.enums.Specialization;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecialization(Specialization specialization);
    List<Doctor> findByAvailableForOnlineConsultTrue();

    @Query("SELECT d FROM Doctor d WHERE d.specialization = :spec AND d.availableForOnlineConsult = true ORDER BY d.averageRating DESC")
    List<Doctor> findAvailableBySpecialization(Specialization spec);

    @Query("SELECT d FROM Doctor d WHERE LOWER(d.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Doctor> searchByName(String name);

    @Query("SELECT d FROM Doctor d WHERE " +
            "(:spec IS NULL OR d.specialization = :spec) AND " +
            "(:minExp IS NULL OR d.experienceYears >= :minExp) AND " +
            "(:maxFee IS NULL OR d.consultationFee <= :maxFee) AND " +
            "(:name IS NULL OR LOWER(d.fullName) LIKE LOWER(CONCAT('%',:name,'%')))")
    List<Doctor> filterDoctors(
            @Param("spec") Specialization spec,
            @Param("minExp") Integer minExp,
            @Param("maxFee") Double maxFee,
            @Param("name") String name
    );
}
