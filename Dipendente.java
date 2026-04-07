// Classe per i dipendenti del bar
public class Dipendente {
    private String nome;
    private String turno;

    public Dipendente(String nome) {
        this.nome = nome;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getNome() { return nome; }
    public String getTurno() { return turno; }
}