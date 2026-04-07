// Classe per le voci del menu, tipo un caffè o un panino
public class MenuItem {
    private String nome;
    private double prezzo;

    public MenuItem(String nome, double prezzo) {
        this.nome = nome;
        this.prezzo = prezzo;
    }

    // Getter per nome e prezzo
    public String getNome() { return nome; }
    public double getPrezzo() { return prezzo; }
}