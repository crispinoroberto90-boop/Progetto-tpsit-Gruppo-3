package com.bar.controller;

import com.bar.model.Payment;
import com.bar.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagamenti")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PostMapping
    public ResponseEntity<Payment> processPayment(@RequestBody Map<String, Object> payload) {
        Long orderId = Long.parseLong(payload.get("ordineId").toString());
        Double amount = Double.parseDouble(payload.get("importo").toString());
        String method = payload.get("metodo").toString();

        Payment payment = paymentService.processPayment(orderId, amount, method);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getPaymentStats() {
        Map<String, Object> stats = paymentService.getPaymentStats();
        return ResponseEntity.ok(stats);
    }
}
