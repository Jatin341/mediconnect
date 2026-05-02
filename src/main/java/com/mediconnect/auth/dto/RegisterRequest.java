package com.mediconnect.auth.dto;

import com.mediconnect.user.enums.Role;
import com.mediconnect.user.enums.Specialization;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String fullName;

    @Email @NotBlank
    private String email;

    @NotBlank @Size(min = 6)
    private String password;

    private String phone;

    @NotNull
    private Role role;

    // Doctor-specific (optional for patient)
    private Specialization specialization;
    private Integer experienceYears;
    private String qualification;
    private String hospitalName;
    private Double consultationFee;
    private String bio;
}