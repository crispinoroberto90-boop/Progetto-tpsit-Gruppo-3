package com.bar.service;

import com.bar.model.MenuItem;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private List<MenuItem> menu;
    private int nextId = 18;

    public MenuService() {
        this.menu = new ArrayList<>();
        initializeMenu();
    }

    private void initializeMenu() {
        // BEVANDE
        menu.add(new MenuItem(1L, "Caffe", 1.50, "Bevande"));
        menu.add(new MenuItem(2L, "Cappuccino", 2.00, "Bevande"));
        menu.add(new MenuItem(3L, "Espresso", 1.00, "Bevande"));
        menu.add(new MenuItem(4L, "Latte", 2.50, "Bevande"));
        menu.add(new MenuItem(5L, "Acqua", 0.50, "Bevande"));
        menu.add(new MenuItem(6L, "Bibita", 2.00, "Bevande"));

        // PANINI
        menu.add(new MenuItem(7L, "Panino Prosciutto", 4.00, "Panini"));
        menu.add(new MenuItem(8L, "Panino Formaggio", 3.50, "Panini"));
        menu.add(new MenuItem(9L, "Panino Mortadella", 3.80, "Panini"));
        menu.add(new MenuItem(10L, "Panino Pollo", 4.50, "Panini"));

        // DOLCI
        menu.add(new MenuItem(11L, "Cornetto", 1.20, "Dolci"));
        menu.add(new MenuItem(12L, "Brioche", 1.50, "Dolci"));
        menu.add(new MenuItem(13L, "Torta", 3.00, "Dolci"));
        menu.add(new MenuItem(14L, "Biscotti", 1.00, "Dolci"));

        // SNACK
        menu.add(new MenuItem(15L, "Pizza al taglio", 2.50, "Snack"));
        menu.add(new MenuItem(16L, "Focaccia", 2.00, "Snack"));
        menu.add(new MenuItem(17L, "Taralli", 1.50, "Snack"));
    }

    public List<MenuItem> getAllItems() {
        return new ArrayList<>(menu);
    }

    public MenuItem addItem(MenuItem item) {
        item.setId((long) nextId++);
        menu.add(item);
        return item;
    }

    public void deleteItem(Long id) {
        menu.removeIf(item -> item.getId().equals(id));
    }
}
