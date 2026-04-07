import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Classe per gli ordini, sia al banco che ai tavoli
public class Order {
    private List<OrderItem> elementi;
    private String tipo; // "banco" o "tavolo"
    private int id;
    private LocalDateTime dataOra;
    private static int contatore = 1;

    public Order(String tipo) {
        this.tipo = tipo;
        this.elementi = new ArrayList<>();
        this.id = contatore++;
        this.dataOra = LocalDateTime.now();
    }

    public void aggiungiElemento(OrderItem elemento) {
        elementi.add(elemento);
    }

    public List<OrderItem> getElementi() { return elementi; }
    public String getTipo() { return tipo; }
    public int getId() { return id; }
    public LocalDateTime getDataOra() { return dataOra; }
    
    public String getDataOraFormattata() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dataOra.format(formatter);
    }
}