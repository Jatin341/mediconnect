package com.mediconnect.auth.service;

import com.mediconnect.auth.dto.LoginRequest;
import com.mediconnect.auth.dto.RegisterRequest;
import com.mediconnect.auth.dto.AuthResponse;
import com.mediconnect.notification.service.EmailService;
import com.mediconnect.security.JwtUtil;
import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.entity.Patient;
import com.mediconnect.user.entity.User;
import com.mediconnect.user.enums.Role;
import com.mediconnect.user.repository.DoctorRepository;
import com.mediconnect.user.repository.PatientRepository;
import com.mediconnect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User savedUser;

        if (request.getRole() == Role.DOCTOR) {
            Doctor doctor = Doctor.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .password(encodedPassword)
                    .phone(request.getPhone())
                    .role(Role.DOCTOR)
                    .specialization(request.getSpecialization())
                    .experienceYears(request.getExperienceYears())
                    .qualification(request.getQualification())
                    .hospitalName(request.getHospitalName())
                    .consultationFee(request.getConsultationFee())
                    .bio(request.getBio())
                    .build();
            savedUser = doctorRepository.save(doctor);

        } else if (request.getRole() == Role.ADMIN) {
            User admin = User.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .password(encodedPassword)
                    .phone(request.getPhone())
                    .role(Role.ADMIN)
                    .build();
            savedUser = userRepository.save(admin);

        } else {
            Patient patient = Patient.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .password(encodedPassword)
                    .phone(request.getPhone())
                    .role(Role.PATIENT)
                    .build();
            savedUser = patientRepository.save(patient);
        }

        // Email send
        try {
            emailService.sendWelcomeEmail(
                    savedUser.getEmail(),
                    savedUser.getFullName(),
                    savedUser.getRole().name()
            );
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }

        return buildAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        String refresh = jwtUtil.generateRefreshToken(user.getEmail());
        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken(refresh)
                .email(user.getEmail())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }
}