package com.mediconnect.medical.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mediconnect.medical.dto.PrescriptionItem;
import com.mediconnect.medical.dto.PrescriptionRequest;
import com.mediconnect.user.entity.Doctor;
import com.mediconnect.user.entity.Patient;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class PdfGeneratorService {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(59, 130, 246));
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.DARK_GRAY);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.GRAY);

    public byte[] generatePrescriptionPdf(PrescriptionRequest request,
                                          Doctor doctor, Patient patient) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 40, 40, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            // ===== HEADER =====
            addHeader(document, doctor);

            // ===== PATIENT INFO =====
            addPatientInfo(document, patient);

            // ===== MEDICINES TABLE =====
            addMedicinesTable(document, request.getMedicines());

            // ===== NOTES =====
            if (request.getNotes() != null && !request.getNotes().isEmpty()) {
                addNotes(document, request.getNotes());
            }

            // ===== FOOTER =====
            addFooter(document, doctor);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed: " + e.getMessage());
        }
    }

    private void addHeader(Document doc, Doctor doctor) throws DocumentException {
        // Hospital name
        Paragraph hospitalName = new Paragraph("🏥 " + (doctor.getHospitalName() != null ? doctor.getHospitalName() : "MediConnect Clinic"), TITLE_FONT);
        hospitalName.setAlignment(Element.ALIGN_CENTER);
        doc.add(hospitalName);

        // Doctor info
        Paragraph doctorInfo = new Paragraph(
                "Dr. " + doctor.getFullName() + " | " + (doctor.getSpecialization() != null ? doctor.getSpecialization().toString().replace("_", " ") : "") +
                        " | " + (doctor.getQualification() != null ? doctor.getQualification() : ""),
                NORMAL_FONT
        );
        doctorInfo.setAlignment(Element.ALIGN_CENTER);
        doc.add(doctorInfo);

        // Divider
        doc.add(new Paragraph(" "));
        PdfPTable divider = new PdfPTable(1);
        divider.setWidthPercentage(100);
        PdfPCell dividerCell = new PdfPCell(new Phrase("PRESCRIPTION", HEADER_FONT));
        dividerCell.setBackgroundColor(new BaseColor(59, 130, 246));
        dividerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        dividerCell.setPadding(8);
        dividerCell.setBorder(Rectangle.NO_BORDER);
        divider.addCell(dividerCell);
        doc.add(divider);
        doc.add(new Paragraph(" "));
    }

    private void addPatientInfo(Document doc, Patient patient) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 2f, 1f, 2f});

        addInfoCell(table, "Patient:", true);
        addInfoCell(table, patient.getFullName(), false);
        addInfoCell(table, "Date:", true);
        addInfoCell(table, LocalDate.now().toString(), false);

        addInfoCell(table, "Age/Gender:", true);
        addInfoCell(table, (patient.getGender() != null ? patient.getGender() : "N/A"), false);
        addInfoCell(table, "Blood Group:", true);
        addInfoCell(table, (patient.getBloodGroup() != null ? patient.getBloodGroup() : "N/A"), false);

        doc.add(table);
        doc.add(new Paragraph(" "));
    }

    private void addMedicinesTable(Document doc, List<PrescriptionItem> medicines) throws DocumentException {
        // Table header
        Paragraph rxTitle = new Paragraph("Rx — Medicines", BOLD_FONT);
        doc.add(rxTitle);
        doc.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2.5f, 1.2f, 2f, 1.5f, 2.5f});

        // Headers
        String[] headers = {"Medicine Name", "Dosage", "Frequency", "Duration", "Instructions"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, HEADER_FONT));
            cell.setBackgroundColor(new BaseColor(30, 58, 95));
            cell.setPadding(7);
            cell.setBorderColor(BaseColor.WHITE);
            table.addCell(cell);
        }

        // Rows
        boolean alternate = false;
        for (PrescriptionItem item : medicines) {
            BaseColor rowColor = alternate ? new BaseColor(245, 247, 250) : BaseColor.WHITE;
            addMedCell(table, item.getMedicineName(), rowColor);
            addMedCell(table, item.getDosage() != null ? item.getDosage() : "-", rowColor);
            addMedCell(table, item.getFrequency() != null ? item.getFrequency() : "-", rowColor);
            addMedCell(table, item.getDuration() != null ? item.getDuration() : "-", rowColor);
            addMedCell(table, item.getInstructions() != null ? item.getInstructions() : "-", rowColor);
            alternate = !alternate;
        }

        doc.add(table);
        doc.add(new Paragraph(" "));
    }

    private void addNotes(Document doc, String notes) throws DocumentException {
        PdfPTable notesTable = new PdfPTable(1);
        notesTable.setWidthPercentage(100);
        PdfPCell notesCell = new PdfPCell();
        notesCell.addElement(new Phrase("Doctor's Notes:", BOLD_FONT));
        notesCell.addElement(new Phrase(notes, NORMAL_FONT));
        notesCell.setPadding(10);
        notesCell.setBackgroundColor(new BaseColor(255, 252, 235));
        notesTable.addCell(notesCell);
        doc.add(notesTable);
        doc.add(new Paragraph(" "));
    }

    private void addFooter(Document doc, Doctor doctor) throws DocumentException {
        doc.add(new Paragraph(" "));
        Paragraph sig = new Paragraph("_________________________", NORMAL_FONT);
        sig.setAlignment(Element.ALIGN_RIGHT);
        doc.add(sig);

        Paragraph sigName = new Paragraph("Dr. " + doctor.getFullName(), BOLD_FONT);
        sigName.setAlignment(Element.ALIGN_RIGHT);
        doc.add(sigName);

        Paragraph disclaimer = new Paragraph(
                "This prescription is computer generated. Valid for 30 days from date of issue.",
                SMALL_FONT
        );
        disclaimer.setAlignment(Element.ALIGN_CENTER);
        doc.add(new Paragraph(" "));
        doc.add(disclaimer);
    }

    private void addInfoCell(PdfPTable table, String text, boolean isLabel) {
        PdfPCell cell = new PdfPCell(new Phrase(text, isLabel ? BOLD_FONT : NORMAL_FONT));
        cell.setPadding(5);
        cell.setBorderColor(new BaseColor(220, 220, 220));
        if (isLabel) cell.setBackgroundColor(new BaseColor(245, 247, 250));
        table.addCell(cell);
    }

    private void addMedCell(PdfPTable table, String text, BaseColor bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setPadding(6);
        cell.setBackgroundColor(bg);
        cell.setBorderColor(new BaseColor(220, 220, 220));
        table.addCell(cell);
    }
}