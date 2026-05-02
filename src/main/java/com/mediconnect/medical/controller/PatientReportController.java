package com.mediconnect.medical.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class PatientReportController {

    private static final String UPLOAD_DIR = "uploads/reports/";

    @PostMapping("/upload/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ResponseEntity<Map<String, String>> uploadReport(
            @PathVariable Long patientId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "Medical Report") String reportType)
            throws IOException {

        Path uploadPath = Paths.get(UPLOAD_DIR + patientId + "/");
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(),
                uploadPath.resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok(Map.of(
                "message", "Report uploaded successfully",
                "fileName", fileName,
                "fileUrl", "/uploads/reports/" + patientId + "/" + fileName,
                "reportType", reportType
        ));
    }

    @GetMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<Map<String, String>>> getReports(
            @PathVariable Long patientId) throws IOException {

        Path uploadPath = Paths.get(UPLOAD_DIR + patientId + "/");
        List<Map<String, String>> files = new ArrayList<>();

        if (Files.exists(uploadPath)) {
            Files.list(uploadPath).forEach(file -> files.add(Map.of(
                    "fileName", file.getFileName().toString(),
                    "fileUrl", "/uploads/reports/" + patientId + "/" + file.getFileName(),
                    "size", String.valueOf(file.toFile().length())
            )));
        }
        return ResponseEntity.ok(files);
    }
}