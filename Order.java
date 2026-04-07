import java.util.ArrayList;
import java.util.List;

// Classe per gli ordini, sia al banco che ai tavoli
public class Order {
    private List<OrderItem> elementi;
    private String tipo; // "banco" o "tavolo"

    public Order(String tipo) {
        this.tipo = tipo;
        this.elementi = new ArrayList<>();
    }

    public void aggiungiElemento(OrderItem elemento) {
        elementi.add(elemento);
    }

    public List<OrderItem> getElementi() { return elementi; }
    public String getTipo() { return tipo; }
}