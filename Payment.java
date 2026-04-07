// Classe per i pagamenti, contanti o digitale
public class Payment {
    private double importo;
    private String metodo; // "contanti" o "digitale"

    public Payment(double importo, String metodo) {
        this.importo = importo;
        this.metodo = metodo;
    }

    public double getImporto() { return importo; }
    public String getMetodo() { return metodo; }
}