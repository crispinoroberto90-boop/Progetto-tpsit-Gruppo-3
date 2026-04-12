package com.bar.service;

import com.bar.model.Payment;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class PaymentService {
    private List<Payment> payments;
    private long nextId = 1;

    public PaymentService() {
        this.payments = new ArrayList<>();
    }

    public Payment processPayment(Long orderId, Double amount, String method) {
        Payment payment = new Payment(nextId++, orderId, amount, method, LocalDateTime.now());
        payments.add(payment);
        return payment;
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments);
    }

    public Map<String, Object> getPaymentStats() {
        Map<String, Object> stats = new HashMap<>();
        
        double totalCash = payments.stream()
                .filter(p -> "contanti".equals(p.getMethod()))
                .mapToDouble(Payment::getAmount)
                .sum();

        double totalCard = payments.stream()
                .filter(p -> "carta".equals(p.getMethod()) || "bancomat".equals(p.getMethod()))
                .mapToDouble(Payment::getAmount)
                .sum();

        double totalOther = payments.stream()
                .filter(p -> !("contanti".equals(p.getMethod()) || "carta".equals(p.getMethod()) || "bancomat".equals(p.getMethod())))
                .mapToDouble(Payment::getAmount)
                .sum();

        stats.put("totalCash", totalCash);
        stats.put("totalCard", totalCard);
        stats.put("totalOther", totalOther);
        stats.put("totalRevenue", totalCash + totalCard + totalOther);
        stats.put("paymentCount", payments.size());

        return stats;
    }
}
