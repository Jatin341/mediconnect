package com.mediconnect.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

// 🔥 New imports for PDF attachment
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // ✅ Appointment Confirmation
    @Async
    public void sendAppointmentConfirmation(String toEmail, String patientName,
                                            String doctorName, String dateTime,
                                            String roomId) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("✅ Appointment Confirmed — MediConnect");
        msg.setText(
                "Dear " + patientName + ",\n\n" +
                        "Your appointment has been confirmed!\n\n" +
                        "Doctor: Dr. " + doctorName + "\n" +
                        "Date & Time: " + dateTime + "\n" +
                        "Meeting Room ID: " + roomId + "\n\n" +
                        "Login to MediConnect to start chat or video consultation.\n\n" +
                        "Regards,\nMediConnect Team"
        );
        mailSender.send(msg);
    }

    // ✅ Welcome Email
    @Async
    public void sendWelcomeEmail(String toEmail, String name, String role) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("👋 Welcome to MediConnect!");
        msg.setText(
                "Dear " + name + ",\n\n" +
                        "Welcome to MediConnect — Your Health, Connected!\n\n" +
                        "Your account has been created as: " + role + "\n\n" +
                        "Login at: http://localhost:8081\n\n" +
                        "Regards,\nMediConnect Team"
        );
        mailSender.send(msg);
    }

    // ✅ Appointment Reminder
    @Async
    public void sendAppointmentReminder(String toEmail, String patientName,
                                        String doctorName, String dateTime) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("⏰ Appointment Reminder — MediConnect");
        msg.setText(
                "Dear " + patientName + ",\n\n" +
                        "Reminder: Your appointment with Dr. " + doctorName +
                        " is scheduled at " + dateTime + "\n\n" +
                        "Please login to MediConnect to join the consultation.\n\n" +
                        "Regards,\nMediConnect Team"
        );
        mailSender.send(msg);
    }

    // 🔥 NEW: Prescription Email with PDF attachment
    @Async
    public void sendPrescriptionEmail(String toEmail, String patientName,
                                      String doctorName, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("💊 Your Prescription — MediConnect");
            helper.setText(
                    "Dear " + patientName + ",\n\n" +
                            "Your prescription from Dr. " + doctorName + " is attached.\n\n" +
                            "Please follow the prescribed medicines as directed.\n\n" +
                            "Regards,\nMediConnect Team"
            );

            helper.addAttachment("prescription.pdf",
                    new ByteArrayResource(pdfBytes));

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Prescription email error: " + e.getMessage());
        }
    }

    // 🔥 NEW: Cancellation Email
    @Async
    public void sendCancellationEmail(String toEmail, String patientName,
                                      String doctorName, String dateTime) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("❌ Appointment Cancelled — MediConnect");
        msg.setText(
                "Dear " + patientName + ",\n\n" +
                        "Your appointment with Dr. " + doctorName +
                        " scheduled at " + dateTime + " has been cancelled.\n\n" +
                        "Please book a new appointment at your convenience.\n\n" +
                        "Regards,\nMediConnect Team"
        );
        mailSender.send(msg);
    }

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("🔐 Password Reset OTP — MediConnect");
        msg.setText(
                "Your OTP for password reset is: " + otp + "\n\n" +
                        "Valid for 5 minutes only.\n\n" +
                        "If you didn't request this, ignore this email.\n\n" +
                        "Regards,\nMediConnect Team"
        );
        mailSender.send(msg);
    }
}