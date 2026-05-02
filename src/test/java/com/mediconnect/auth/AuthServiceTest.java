package com.mediconnect.auth;

import com.mediconnect.auth.dto.LoginRequest;
import com.mediconnect.auth.dto.RegisterRequest;
import com.mediconnect.auth.service.AuthService;
import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.entity.Patient;
import com.mediconnect.user.enums.Role;
import com.mediconnect.user.repository.DoctorRepository;
import com.mediconnect.user.repository.PatientRepository;
import com.mediconnect.user.repository.UserRepository;
import com.mediconnect.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock DoctorRepository doctorRepository;
    @Mock PatientRepository patientRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtUtil jwtUtil;
    @Mock AuthenticationManager authenticationManager;

    @InjectMocks AuthService authService;

    @Test
    void register_Patient_Success() {
        RegisterRequest req = new RegisterRequest();
        req.setFullName("Test Patient");
        req.setEmail("test@test.com");
        req.setPassword("test123");
        req.setRole(Role.PATIENT);

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPass");
        when(patientRepository.save(any())).thenAnswer(i -> {
            Patient p = i.getArgument(0);
            p.setId(1L);
            return p;
        });
        when(jwtUtil.generateToken(any(), any())).thenReturn("mockToken");
        when(jwtUtil.generateRefreshToken(any())).thenReturn("mockRefresh");

        var result = authService.register(req);

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
        assertEquals("PATIENT", result.getRole());
        verify(patientRepository, times(1)).save(any());
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("existing@test.com");
        req.setRole(Role.PATIENT);

        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(req));
    }
}