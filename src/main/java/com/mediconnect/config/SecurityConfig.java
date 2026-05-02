package com.mediconnect.config;

import com.mediconnect.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs", "/v3/api-docs/**",
                                "/swagger-ui/**", "/swagger-ui.html",
                                "/swagger-ui/index.html", "/webjars/**",
                                "/ws/**", "/uploads/**",
                                "/", "/index.html",        // ← ye add karo
                                "/*.html", "/*.js", "/*.css", "/*.ico"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/analytics/**").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers("/api/payment/**").hasAnyRole("PATIENT", "ADMIN")
                        .requestMatchers("/api/symptoms/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                        .requestMatchers("/api/doctors/**").hasAnyRole("DOCTOR", "ADMIN", "PATIENT")
                        .requestMatchers("/api/patients/**").hasAnyRole("PATIENT", "ADMIN", "DOCTOR")
                        .requestMatchers("/api/appointments/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                        .requestMatchers("/api/medical/**").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                        .requestMatchers("/api/prescriptions/**").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                        .requestMatchers("/api/reports/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}