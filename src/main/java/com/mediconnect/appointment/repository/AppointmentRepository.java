package com.mediconnect.appointment.repository;

import com.mediconnect.appointment.entity.Appointment;
import com.mediconnect.appointment.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor WHERE a.patient.id = :patientId ORDER BY a.appointmentTime DESC")
    List<Appointment> findByPatientIdOrderByAppointmentTimeDesc(Long patientId);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor WHERE a.doctor.id = :doctorId ORDER BY a.appointmentTime DESC")
    List<Appointment> findByDoctorIdOrderByAppointmentTimeDesc(Long doctorId);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor WHERE a.doctor.id = :doctorId AND a.status = :status")
    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor WHERE a.doctor.id = :doctorId AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findDoctorAppointmentsInRange(Long doctorId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor WHERE a.meetingRoomId = :roomId")
    Optional<Appointment> findByMeetingRoomId(String roomId);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor WHERE a.status = :status ORDER BY a.appointmentTime ASC")
    List<Appointment> findByStatusOrderByAppointmentTimeAsc(AppointmentStatus status);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor")
    List<Appointment> findAllWithDetails();

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor " +
            "WHERE a.status = 'CONFIRMED' AND a.appointmentTime BETWEEN :now AND :next24h")
    List<Appointment> findUpcomingAppointments(LocalDateTime now, LocalDateTime next24h);
}