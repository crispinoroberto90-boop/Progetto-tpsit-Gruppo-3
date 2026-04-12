package com.bar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Long id;
    private Long orderId;
    private Double amount;
    private String method; // "contanti", "carta", "bancomat", "paypal"
    private LocalDateTime data;
}
