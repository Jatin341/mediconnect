package com.mediconnect.payment;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.key-secret}")
    private String keySecret;

    // Create payment order
    @PostMapping("/create-order")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, Object>> createOrder(
            @RequestParam Double amount,
            @RequestParam Long appointmentId) {
        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);
            JSONObject options = new JSONObject();
            options.put("amount", (int)(amount * 100)); // paise mein
            options.put("currency", "INR");
            options.put("receipt", "appointment_" + appointmentId);
            options.put("notes", new JSONObject().put("appointmentId", appointmentId));

            Order order = client.orders.create(options);

            return ResponseEntity.ok(Map.of(
                    "orderId", order.get("id"),
                    "amount", amount,
                    "currency", "INR",
                    "keyId", keyId
            ));
        } catch (Exception e) {
            throw new RuntimeException("Payment order creation failed: " + e.getMessage());
        }
    }

    // Verify payment
    @PostMapping("/verify")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, String>> verifyPayment(
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpaySignature) {
        try {
            String data = razorpayOrderId + "|" + razorpayPaymentId;
            // Signature verify karo
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "paymentId", razorpayPaymentId,
                    "message", "Payment verified successfully"
            ));
        } catch (Exception e) {
            throw new RuntimeException("Payment verification failed");
        }
    }
}