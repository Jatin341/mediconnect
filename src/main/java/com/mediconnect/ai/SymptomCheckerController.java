package com.mediconnect.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/symptoms")
@RequiredArgsConstructor
public class SymptomCheckerController {

    @Value("${anthropic.api.key:}")
    private String anthropicApiKey;

    @PostMapping("/check")
    public ResponseEntity<Map<String, String>> checkSymptoms(
            @RequestBody Map<String, String> request) {

        String symptoms = request.get("symptoms");
        if (symptoms == null || symptoms.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Symptoms required"));
        }

        String suggestion = analyzeSymptoms(symptoms);
        return ResponseEntity.ok(Map.of(
                "symptoms", symptoms,
                "suggestion", suggestion
        ));
    }

    private String analyzeSymptoms(String symptoms) {
        // Rule-based fallback (no API key needed)
        symptoms = symptoms.toLowerCase();

        if (symptoms.contains("chest") || symptoms.contains("heart") ||
                symptoms.contains("palpitation")) {
            return "Based on your symptoms, we recommend consulting a **CARDIOLOGIST**. " +
                    "Chest pain or heart palpitations should be evaluated promptly.";
        } else if (symptoms.contains("skin") || symptoms.contains("rash") ||
                symptoms.contains("acne") || symptoms.contains("itch")) {
            return "Based on your symptoms, we recommend consulting a **DERMATOLOGIST**. " +
                    "Skin conditions are best treated by a skin specialist.";
        } else if (symptoms.contains("headache") || symptoms.contains("migraine") ||
                symptoms.contains("dizziness") || symptoms.contains("seizure")) {
            return "Based on your symptoms, we recommend consulting a **NEUROLOGIST**. " +
                    "Neurological symptoms need proper evaluation.";
        } else if (symptoms.contains("fever") || symptoms.contains("cold") ||
                symptoms.contains("cough") || symptoms.contains("fatigue")) {
            return "Based on your symptoms, we recommend consulting a **GENERAL PHYSICIAN**. " +
                    "General symptoms like fever and cold are handled by GPs.";
        } else if (symptoms.contains("bone") || symptoms.contains("joint") ||
                symptoms.contains("back pain") || symptoms.contains("fracture")) {
            return "Based on your symptoms, we recommend consulting an **ORTHOPEDIC** specialist. " +
                    "Bone and joint issues need orthopedic care.";
        } else if (symptoms.contains("sugar") || symptoms.contains("diabetes") ||
                symptoms.contains("thyroid")) {
            return "Based on your symptoms, we recommend consulting a **DIABETOLOGIST**. " +
                    "Metabolic conditions need specialist care.";
        } else if (symptoms.contains("eye") || symptoms.contains("vision") ||
                symptoms.contains("blur")) {
            return "Based on your symptoms, we recommend consulting an **OPHTHALMOLOGIST**. " +
                    "Eye problems need vision specialist care.";
        } else if (symptoms.contains("ear") || symptoms.contains("nose") ||
                symptoms.contains("throat") || symptoms.contains("tonsil")) {
            return "Based on your symptoms, we recommend consulting an **ENT SPECIALIST**. " +
                    "Ear, nose and throat issues need ENT care.";
        } else if (symptoms.contains("child") || symptoms.contains("infant") ||
                symptoms.contains("baby")) {
            return "Based on your symptoms, we recommend consulting a **PEDIATRICIAN**. " +
                    "Children's health needs specialized pediatric care.";
        } else if (symptoms.contains("anxiety") || symptoms.contains("depression") ||
                symptoms.contains("stress") || symptoms.contains("mental")) {
            return "Based on your symptoms, we recommend consulting a **PSYCHIATRIST**. " +
                    "Mental health is important — please seek professional help.";
        } else {
            return "Based on your symptoms, we recommend starting with a **GENERAL PHYSICIAN** " +
                    "who can refer you to the appropriate specialist if needed.";
        }
    }
}