// Classe per gli elementi di un ordine
public class OrderItem {
    private String nomeProdotto;
    private int quantita;

    public OrderItem(String nomeProdotto, int quantita) {
        this.nomeProdotto = nomeProdotto;
        this.quantita = quantita;
    }

    public String getNomeProdotto() { return nomeProdotto; }
    public int getQuantita() { return quantita; }
}