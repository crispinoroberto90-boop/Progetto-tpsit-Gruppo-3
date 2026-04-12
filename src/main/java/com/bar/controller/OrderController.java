package com.bar.controller;

import com.bar.model.Order;
import com.bar.model.OrderItem;
import com.bar.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordini")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, String> payload) {
        String tipo = payload.get("tipo");
        Order order = orderService.createOrder(tipo);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<Order> addItemToOrder(
            @PathVariable Long id,
            @RequestBody OrderItem item) {
        Order order = orderService.addItemToOrder(id, item);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<Map<String, Double>> getOrderTotal(@PathVariable Long id) {
        double total = orderService.getOrderTotal(id);
        return ResponseEntity.ok(Map.of("total", total));
    }
}
