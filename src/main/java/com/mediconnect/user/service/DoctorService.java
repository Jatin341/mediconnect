package com.mediconnect.user.service;

import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.user.dto.ReviewRequest;
import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.enums.Specialization;
import com.mediconnect.user.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    public List<Doctor> getDoctorsBySpecialization(Specialization spec) {
        return doctorRepository.findBySpecialization(spec);
    }

    public List<Doctor> getAvailableDoctors() {
        return doctorRepository.findByAvailableForOnlineConsultTrue();
    }

    public List<Doctor> searchByName(String name) {
        return doctorRepository.searchByName(name);
    }

    @Transactional
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor doctor = getDoctorById(id);
        doctor.setSpecialization(updatedDoctor.getSpecialization());
        doctor.setExperienceYears(updatedDoctor.getExperienceYears());
        doctor.setQualification(updatedDoctor.getQualification());
        doctor.setHospitalName(updatedDoctor.getHospitalName());
        doctor.setHospitalAddress(updatedDoctor.getHospitalAddress());
        doctor.setConsultationFee(updatedDoctor.getConsultationFee());
        doctor.setBio(updatedDoctor.getBio());
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor toggleAvailability(Long id) {
        Doctor doctor = getDoctorById(id);
        doctor.setAvailableForOnlineConsult(!doctor.isAvailableForOnlineConsult());
        return doctorRepository.save(doctor);
    }

    public List<Doctor> filterDoctors(Specialization spec, Integer minExp,
                                      Double maxFee, String name) {
        return doctorRepository.filterDoctors(spec, minExp, maxFee, name);
    }

    @Transactional
    public Doctor addReview(Long doctorId, ReviewRequest request) {
        Doctor doctor = getDoctorById(doctorId);
        int total = doctor.getTotalReviews() == null ? 0 : doctor.getTotalReviews();
        double avg = doctor.getAverageRating() == null ? 0.0 : doctor.getAverageRating();
        double newAvg = ((avg * total) + request.getRating()) / (total + 1);
        doctor.setAverageRating(Math.round(newAvg * 10.0) / 10.0);
        doctor.setTotalReviews(total + 1);
        return doctorRepository.save(doctor);
    }
}