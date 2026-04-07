import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe principale Bar per gestire tutto il casino del bar secondo il documento SRS.
 * Questa classe gestisce ordini, menu, magazzino, personale, pagamenti e report.
 */
public class Bar {

    // Attributi per gestire menu e prodotti (RF3)
    private List<MenuItem> vociMenu;

    // Attributi per magazzino (RF4)
    private Map<String, Integer> inventario; // Prodotto -> Quantità

    // Attributi per personale e turni (RF5)
    private List<Employee> dipendenti;

    // Attributi per ordini (RF1)
    private List<Order> ordini;

    // Attributi per pagamenti (RF2)
    private PaymentSystem sistemaPagamento;

    // Attributi per report (RF6)
    private ReportGenerator generatoreReport;

    // Attributi per sicurezza e autenticazione (RNF2)
    private Map<String, String> credenzialiUtente; // Username -> Password (semplificato, in produzione usare hash)
    private boolean backupAbilitato;

    // Costruttore
    public Bar() {
        this.vociMenu = new ArrayList<>();
        this.inventario = new HashMap<>();
        this.dipendenti = new ArrayList<>();
        this.ordini = new ArrayList<>();
        this.sistemaPagamento = new PaymentSystem();
        this.generatoreReport = new ReportGenerator();
        this.credenzialiUtente = new HashMap<>();
        this.backupAbilitato = true; // Backup periodici abilitati per default
    }

    // Metodo per registrare ordini al banco e ai tavoli (RF1)
    public void registraOrdine(Order ordine) {
        ordini.add(ordine);
        // Aggiorna magazzino se necessario
        aggiornaInventario(ordine);
    }

    // Metodo per gestire pagamenti (RF2)
    public boolean elaboraPagamento(Payment pagamento) {
        return sistemaPagamento.elabora(pagamento);
    }

    // Metodo per aggiungere prodotti al menu (RF3)
    public void aggiungiVoceMenu(MenuItem voce) {
        vociMenu.add(voce);
    }

    // Metodo per rimuovere prodotti dal menu (RF3)
    public void rimuoviVoceMenu(MenuItem voce) {
        vociMenu.remove(voce);
    }

    // Metodo per aggiornare scorte magazzino (RF4)
    public void aggiornaInventario(Order ordine) {
        for (OrderItem elemento : ordine.getElementi()) {
            String prodotto = elemento.getNomeProdotto();
            int quantita = elemento.getQuantita();
            if (inventario.containsKey(prodotto)) {
                int scortaAttuale = inventario.get(prodotto);
                inventario.put(prodotto, scortaAttuale - quantita);
                if (inventario.get(prodotto) < 10) { // Avviso quando scorte basse
                    System.out.println("Avviso: Scorte basse per " + prodotto);
                }
            }
        }
    }

    // Metodo per aggiungere scorte manualmente
    public void aggiungiInventario(String prodotto, int quantita) {
        inventario.put(prodotto, inventario.getOrDefault(prodotto, 0) + quantita);
    }

    // Metodo per gestire personale (RF5)
    public void aggiungiDipendente(Employee dipendente) {
        dipendenti.add(dipendente);
    }

    // Metodo per rimuovere personale (RF5)
    public void rimuoviDipendente(Employee dipendente) {
        dipendenti.remove(dipendente);
    }

    // Metodo per assegnare turni (RF5) - semplificato
    public void assegnaTurno(Employee dipendente, String turno) {
        dipendente.setTurno(turno);
    }

    // Metodo per generare report (RF6)
    public void generaReportVendite() {
        generatoreReport.generaReportVendite(ordini);
    }

    public void generaReportIncassi() {
        generatoreReport.generaReportIncassi(ordini);
    }

    // Metodo per autenticazione utenti (RNF2)
    public boolean autenticaUtente(String nomeUtente, String password) {
        return credenzialiUtente.containsKey(nomeUtente) && credenzialiUtente.get(nomeUtente).equals(password);
    }

    // Metodo per aggiungere credenziali utente
    public void aggiungiCredenzialeUtente(String nomeUtente, String password) {
        credenzialiUtente.put(nomeUtente, password);
    }

    // Metodo per backup (RNF2) - semplificato
    public void eseguiBackup() {
        if (backupAbilitato) {
            System.out.println("Backup eseguito.");
            // Logica di backup reale qui
        }
    }

    // Metodo per scalabilità: aggiungere punti vendita (RNF4) - placeholder
    public void aggiungiFiliale(Bar filiale) {
        // Logica per gestire più punti vendita
        System.out.println("Punto vendita aggiunto.");
    }

    // Getter per prestazioni e usabilità (RNF1, RNF3)
    public List<MenuItem> getVociMenu() {
        return vociMenu;
    }

    public Map<String, Integer> getInventario() {
        return inventario;
    }

    public List<Employee> getDipendenti() {
        return dipendenti;
    }

    public List<Order> getOrdini() {
        return ordini;
    }


}