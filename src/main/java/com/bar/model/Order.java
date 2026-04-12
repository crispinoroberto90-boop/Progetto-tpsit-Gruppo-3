package com.bar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String tipo; // "banco" o "tavolo"
    private LocalDateTime dataOra;
    private List<OrderItem> items;

    public Order(Long id, String tipo, LocalDateTime dataOra) {
        this.id = id;
        this.tipo = tipo;
        this.dataOra = dataOra;
        this.items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
