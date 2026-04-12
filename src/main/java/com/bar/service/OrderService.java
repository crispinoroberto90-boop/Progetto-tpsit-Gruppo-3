package com.bar.service;

import com.bar.model.Order;
import com.bar.model.OrderItem;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private List<Order> orders;
    private long nextId = 1;

    public OrderService() {
        this.orders = new ArrayList<>();
    }

    public Order createOrder(String tipo) {
        Order order = new Order(nextId++, tipo, LocalDateTime.now());
        orders.add(order);
        return order;
    }

    public Order addItemToOrder(Long orderId, OrderItem item) {
        Order order = orders.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElse(null);

        if (order != null) {
            order.addItem(item);
        }
        return order;
    }

    public Order getOrderById(Long id) {
        return orders.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public double getOrderTotal(Long orderId) {
        Order order = getOrderById(orderId);
        if (order == null) return 0.0;
        
        return order.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
