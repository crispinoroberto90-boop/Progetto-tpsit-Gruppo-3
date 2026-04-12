package com.bar.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {
    private Map<String, Integer> inventory;

    public InventoryService() {
        this.inventory = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        inventory.put("Caffe", 50);
        inventory.put("Cappuccino", 45);
        inventory.put("Espresso", 60);
        inventory.put("Latte", 40);
        inventory.put("Acqua", 100);
        inventory.put("Bibita", 80);
        inventory.put("Panino Prosciutto", 30);
        inventory.put("Panino Formaggio", 25);
        inventory.put("Panino Mortadella", 28);
        inventory.put("Panino Pollo", 32);
        inventory.put("Cornetto", 50);
        inventory.put("Brioche", 45);
        inventory.put("Torta", 15);
        inventory.put("Biscotti", 60);
        inventory.put("Pizza al taglio", 40);
        inventory.put("Focaccia", 35);
        inventory.put("Taralli", 50);
    }

    public Map<String, Integer> getInventory() {
        return new HashMap<>(inventory);
    }

    public void addStock(String product, int quantity) {
        inventory.put(product, inventory.getOrDefault(product, 0) + quantity);
    }

    public void removeStock(String product, int quantity) {
        int current = inventory.getOrDefault(product, 0);
        inventory.put(product, Math.max(0, current - quantity));
    }

    public int getStock(String product) {
        return inventory.getOrDefault(product, 0);
    }
}
