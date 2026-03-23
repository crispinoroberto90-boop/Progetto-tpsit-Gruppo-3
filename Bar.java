import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe madre Bar che implementa il sistema di gestione bar secondo il documento SRS.
 * Questa classe gestisce ordini, menu, magazzino, personale, pagamenti e report.
 */
public class Bar {

    // Attributi per gestire menu e prodotti (RF3)
    private List<MenuItem> menu;

    // Attributi per magazzino (RF4)
    private Map<String, Integer> inventory; // Prodotto -> Quantità

    // Attributi per personale e turni (RF5)
    private List<Employee> employees;

    // Attributi per ordini (RF1)
    private List<Order> orders;

    // Attributi per pagamenti (RF2)
    private PaymentSystem paymentSystem;

    // Attributi per report (RF6)
    private ReportGenerator reportGenerator;

    // Attributi per sicurezza e autenticazione (RNF2)
    private Map<String, String> userCredentials; // Username -> Password (semplificato, in produzione usare hash)
    private boolean backupEnabled;

    // Costruttore
    public Bar() {
        this.menu = new ArrayList<>();
        this.inventory = new HashMap<>();
        this.employees = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.paymentSystem = new PaymentSystem();
        this.reportGenerator = new ReportGenerator();
        this.userCredentials = new HashMap<>();
        this.backupEnabled = true; // Backup periodici abilitati per default
    }

    // Metodo per registrare ordini al banco e ai tavoli (RF1)
    public void registerOrder(Order order) {
        orders.add(order);
        // Aggiorna magazzino se necessario
        updateInventory(order);
    }

    // Metodo per gestire pagamenti (RF2)
    public boolean processPayment(Payment payment) {
        return paymentSystem.process(payment);
    }

    // Metodo per aggiungere prodotti al menu (RF3)
    public void addMenuItem(MenuItem item) {
        menu.add(item);
    }

    // Metodo per rimuovere prodotti dal menu (RF3)
    public void removeMenuItem(MenuItem item) {
        menu.remove(item);
    }

    // Metodo per aggiornare scorte magazzino (RF4)
    public void updateInventory(Order order) {
        for (OrderItem item : order.getItems()) {
            String product = item.getProductName();
            int quantity = item.getQuantity();
            if (inventory.containsKey(product)) {
                int currentStock = inventory.get(product);
                inventory.put(product, currentStock - quantity);
                if (inventory.get(product) < 10) { // Avviso quando scorte basse
                    System.out.println("Avviso: Scorte basse per " + product);
                }
            }
        }
    }

    // Metodo per aggiungere scorte manualmente
    public void addInventory(String product, int quantity) {
        inventory.put(product, inventory.getOrDefault(product, 0) + quantity);
    }

    // Metodo per gestire personale (RF5)
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    // Metodo per rimuovere personale (RF5)
    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    // Metodo per assegnare turni (RF5) - semplificato
    public void assignShift(Employee employee, String shift) {
        employee.setShift(shift);
    }

    // Metodo per generare report (RF6)
    public void generateSalesReport() {
        reportGenerator.generateSalesReport(orders);
    }

    public void generateRevenueReport() {
        reportGenerator.generateRevenueReport(orders);
    }

    // Metodo per autenticazione utenti (RNF2)
    public boolean authenticateUser(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    // Metodo per aggiungere credenziali utente
    public void addUserCredential(String username, String password) {
        userCredentials.put(username, password);
    }

    // Metodo per backup (RNF2) - semplificato
    public void performBackup() {
        if (backupEnabled) {
            System.out.println("Backup eseguito.");
            // Logica di backup reale qui
        }
    }

    // Metodo per scalabilità: aggiungere punti vendita (RNF4) - placeholder
    public void addBranch(Bar branch) {
        // Logica per gestire più punti vendita
        System.out.println("Punto vendita aggiunto.");
    }

    // Getter per prestazioni e usabilità (RNF1, RNF3)
    public List<MenuItem> getMenu() {
        return menu;
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Order> getOrders() {
        return orders;
    }

    // Classi interne semplificate per rappresentare entità
    public static class MenuItem {
        private String name;
        private double price;

        public MenuItem(String name, double price) {
            this.name = name;
            this.price = price;
        }

        // Getter e setter
        public String getName() { return name; }
        public double getPrice() { return price; }
    }

    public static class Employee {
        private String name;
        private String shift;

        public Employee(String name) {
            this.name = name;
        }

        public void setShift(String shift) {
            this.shift = shift;
        }

        public String getName() { return name; }
        public String getShift() { return shift; }
    }

    public static class Order {
        private List<OrderItem> items;
        private String type; // "banco" o "tavolo"

        public Order(String type) {
            this.type = type;
            this.items = new ArrayList<>();
        }

        public void addItem(OrderItem item) {
            items.add(item);
        }

        public List<OrderItem> getItems() { return items; }
        public String getType() { return type; }
    }

    public static class OrderItem {
        private String productName;
        private int quantity;

        public OrderItem(String productName, int quantity) {
            this.productName = productName;
            this.quantity = quantity;
        }

        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
    }

    public static class Payment {
        private double amount;
        private String method; // "contanti" o "digitale"

        public Payment(double amount, String method) {
            this.amount = amount;
            this.method = method;
        }

        public double getAmount() { return amount; }
        public String getMethod() { return method; }
    }

    // Classi di supporto
    public static class PaymentSystem {
        public boolean process(Payment payment) {
            // Logica semplificata per processare pagamento
            System.out.println("Pagamento di " + payment.getAmount() + " elaborato con metodo " + payment.getMethod());
            return true;
        }
    }

    public static class ReportGenerator {
        public void generateSalesReport(List<Order> orders) {
            // Logica semplificata per report vendite
            System.out.println("Report vendite generato.");
        }

        public void generateRevenueReport(List<Order> orders) {
            // Logica semplificata per report incassi
            System.out.println("Report incassi generato.");
        }
    }
}