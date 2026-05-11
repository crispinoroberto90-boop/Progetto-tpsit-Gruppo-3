// File di prova per testare il sistema Bar con menu interattivo
// Tipo, scegli cosa fare e vediamo se funziona

import java.util.List;
import java.util.Scanner;

public class Main {
    // PASSWORD DI SICUREZZA PER OPERAZIONI SENSIBILI (puoi cambiarla qui)
    private static final String PASSWORD_SICUREZZA = "barbello";
    
    public static void main(String[] args) {
        Scanner tastiera = new Scanner(System.in);
        Bar bar = new Bar();
        Order ordineCorrente = null; // Per creare ordini step by step

        System.out.println("Benvenuto nel sistema Bar! Scegli un'opzione:");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Aggiungi voce al menu");
            System.out.println("2. Rimuovi voce dal menu");
            System.out.println("3. Aggiungi dipendente");
            System.out.println("4. Crea nuovo ordine");
            System.out.println("5. Aggiungi elemento all'ordine corrente");
            System.out.println("6. Registra ordine corrente");
            System.out.println("7. Elabora pagamento");
            System.out.println("8. Genera report vendite");
            System.out.println("9. Genera report incassi");
            System.out.println("10. Aggiungi scorte inventario");
            System.out.println("11. Mostra menu attuale");
            System.out.println("12. Mostra dipendenti");
            System.out.println("13. Mostra numero ordini");
            System.out.println("14. Mostra inventario");
            System.out.println("15. Mostra riepilogo ordini (totali)");
            System.out.println("16. Mostra dettagli ordine specifico");
            System.out.println("17. Carica menu del giorno");
            System.out.println("0. Esci");

            System.out.print("Scegli: ");
            int scelta = tastiera.nextInt();
            tastiera.nextLine(); // Per il newline

            switch (scelta) {
                case 1:
                    // Operazione sensibile - richiede password
                    if (!verificaPassword(tastiera)) {
                        break;
                    }
                    System.out.print("Nome voce: ");
                    String nomeVoce = tastiera.nextLine();
                    System.out.print("Prezzo: ");
                    double prezzo = tastiera.nextDouble();
                    bar.aggiungiVoceMenu(new MenuItem(nomeVoce, prezzo));
                    System.out.println("Voce aggiunta!");
                    break;
                case 2:
                    System.out.print("Nome voce da rimuovere: ");
                    String nomeRimuovi = tastiera.nextLine();
                    // Trova e rimuovi (semplificato)
                    bar.getVociMenu().removeIf(v -> v.getNome().equals(nomeRimuovi));
                    System.out.println("Voce rimossa!");
                    break;
                case 3:
                    System.out.print("Nome dipendente: ");
                    String nomeDip = tastiera.nextLine();
                    Dipendente dip = new Dipendente(nomeDip);
                    bar.aggiungiDipendente(dip);
                    System.out.print("Turno (es. mattina): ");
                    String turno = tastiera.nextLine();
                    bar.assegnaTurno(dip, turno);
                    System.out.println("Dipendente aggiunto!");
                    break;
                case 4:
                    System.out.print("Tipo ordine (banco/tavolo): ");
                    String tipo = tastiera.nextLine();
                    ordineCorrente = new Order(tipo);
                    System.out.println("Ordine creato! Tipo: " + tipo);
                    
                    // Loop per aggiungere elementi all'ordine
                    boolean aggiungiAltri = true;
                    while (aggiungiAltri) {
                        // Mostra il menu del giorno e fa scegliere
                        List<MenuItem> menuGiorno = MenuGiorno.getMenuDelGiorno();
                        System.out.println("\nScegli dal menu del giorno:");
                        for (int i = 0; i < menuGiorno.size(); i++) {
                            MenuItem item = menuGiorno.get(i);
                            System.out.println((i + 1) + ". " + item.getNome() + " (" + item.getPrezzo() + "€)");
                        }
                        System.out.print("Scegli numero prodotto (0 per tornare al menu): ");
                        int sceltaProd = tastiera.nextInt();
                        
                        if (sceltaProd == 0) {
                            // Torna al menu principale
                            aggiungiAltri = false;
                            break;
                        }
                        
                        if (sceltaProd > 0 && sceltaProd <= menuGiorno.size()) {
                            MenuItem itemScelto = menuGiorno.get(sceltaProd - 1);
                            System.out.print("Quantita: ");
                            int quant = tastiera.nextInt();
                            ordineCorrente.aggiungiElemento(new OrderItem(itemScelto.getNome(), quant));
                            System.out.println("Elemento aggiunto: " + itemScelto.getNome() + " x" + quant);
                            
                            // Mostra il riepilogo dell'ordine in costruzione
                            bar.mostraRiepilogoOrdineInCostruzione(ordineCorrente);
                            
                            // Chiedi se continuare o confermare
                            System.out.print("Premi 5 per aggiungere altri elementi, 6 per confermare e registrare: ");
                            int sceltaContinua = tastiera.nextInt();
                            
                            if (sceltaContinua == 6) {
                                // Registra l'ordine
                                if (!ordineCorrente.getElementi().isEmpty()) {
                                    bar.registraOrdine(ordineCorrente);
                                    System.out.println("Ordine #" + ordineCorrente.getId() + " registrato!");
                                    ordineCorrente = null;
                                    aggiungiAltri = false;
                                }
                            }
                            // Se premi 5, il loop continua
                        } else {
                            System.out.println("Scelta non valida!");
                        }
                    }
                    break;
                case 5:
                    if (ordineCorrente == null) {
                        System.out.println("Prima crea un ordine!");
                        break;
                    }
                    // Mostra il menu del giorno e fa scegliere
                    List<MenuItem> menuGiorno = MenuGiorno.getMenuDelGiorno();
                    System.out.println("\nScegli dal menu del giorno:");
                    for (int i = 0; i < menuGiorno.size(); i++) {
                        MenuItem item = menuGiorno.get(i);
                        System.out.println((i + 1) + ". " + item.getNome() + " (" + item.getPrezzo() + "€)");
                    }
                    System.out.print("Scegli numero prodotto (0 per annullare): ");
                    int sceltaProd = tastiera.nextInt();
                    if (sceltaProd > 0 && sceltaProd <= menuGiorno.size()) {
                        MenuItem itemScelto = menuGiorno.get(sceltaProd - 1);
                        System.out.print("Quantita: ");
                        int quant = tastiera.nextInt();
                        ordineCorrente.aggiungiElemento(new OrderItem(itemScelto.getNome(), quant));
                        System.out.println("Elemento aggiunto: " + itemScelto.getNome() + " x" + quant);
                        // Mostra il riepilogo dell'ordine in costruzione
                        bar.mostraRiepilogoOrdineInCostruzione(ordineCorrente);
                    } else if (sceltaProd != 0) {
                        System.out.println("Scelta non valida!");
                    }
                    break;
                case 6:
                    if (ordineCorrente == null) {
                        System.out.println("Nessun ordine corrente!");
                        break;
                    }
                    if (ordineCorrente.getElementi().isEmpty()) {
                        System.out.println("Errore: l'ordine è vuoto! Aggiungi almeno un elemento prima di registrare.");
                        break;
                    }
                    bar.registraOrdine(ordineCorrente);
                    System.out.println("Ordine #" + ordineCorrente.getId() + " registrato!");
                    ordineCorrente = null;
                    break;
                case 7:
                    System.out.print("Importo: ");
                    double imp = tastiera.nextDouble();
                    tastiera.nextLine();
                    System.out.print("Metodo (contanti/digitale): ");
                    String met = tastiera.nextLine();
                    Payment pag = new Payment(imp, met);
                    boolean ok = bar.elaboraPagamento(pag);
                    System.out.println("Pagamento " + (ok ? "OK" : "Errore"));
                    break;
                case 8:
                    bar.generaReportVendite();
                    break;
                case 9:
                    bar.generaReportIncassi();
                    break;
                case 10:
                    // Operazione sensibile - richiede password
                    if (!verificaPassword(tastiera)) {
                        break;
                    }
                    System.out.print("Prodotto: ");
                    String prodInv = tastiera.nextLine();
                    System.out.print("Quantita: ");
                    int quantInv = tastiera.nextInt();
                    bar.aggiungiInventario(prodInv, quantInv);
                    System.out.println("Scorte aggiunte!");
                    break;
                case 11:
                    // Mostra il menu del giorno in modo bello
                    MenuGiorno.stampaMenuDelGiorno();
                    break;
                case 12:
                    System.out.println("Dipendenti:");
                    for (Object d : bar.getDipendenti()) {
                        System.out.println("- " + ((Dipendente) d).getNome() + " (" + ((Dipendente) d).getTurno() + ")");
                    }
                    break;
                case 13:
                    System.out.println("Ordini: " + bar.getOrdini().size());
                    break;
                case 14:
                    System.out.println("Inventario:");
                    bar.getInventario().forEach((p, q) -> System.out.println("- " + p + ": " + q));
                    break;
                case 15:
                    bar.mostraRiepilogoOrdini();
                    break;
                case 16:
                    if (bar.getOrdini().isEmpty()) {
                        System.out.println("Nessun ordine disponibile!");
                    } else {
                        System.out.println("Ordini disponibili:");
                        for (Order o : bar.getOrdini()) {
                            System.out.println("ID: " + o.getId() + " - " + o.getDataOraFormattata());
                        }
                        System.out.print("Inserisci ID ordine: ");
                        int idOrdine = tastiera.nextInt();
                        boolean trovato = false;
                        for (Order o : bar.getOrdini()) {
                            if (o.getId() == idOrdine) {
                                bar.mostraDettagliOrdine(o);
                                trovato = true;
                                break;
                            }
                        }
                        if (!trovato) {
                            System.out.println("Ordine non trovato!");
                        }
                    }
                    break;
                case 17:
                    // Operazione sensibile - richiede password
                    if (!verificaPassword(tastiera)) {
                        break;
                    }
                    // Carica il menu del giorno nel bar (svuota il menu attuale e carica quello del giorno)
                    bar.getVociMenu().clear(); // Svuota il menu precedente
                    List<MenuItem> menuDelGiorno = MenuGiorno.getMenuDelGiorno();
                    for (MenuItem voce : menuDelGiorno) {
                        bar.aggiungiVoceMenu(voce);
                    }
                    System.out.println("Menu del giorno caricato! (" + menuDelGiorno.size() + " voci)");
                    break;
                case 0:
                    System.out.println("Ciao!");
                    tastiera.close();
                    return;
                default:
                    System.out.println("Scelta non valida!");
            }
        }
    }
    
    // Metodo per verificare la password di sicurezza
    private static boolean verificaPassword(Scanner tastiera) {
        System.out.print("Inserisci password di sicurezza: ");
        String passwordInserita = tastiera.nextLine();
        if (passwordInserita.equals(PASSWORD_SICUREZZA)) {
            System.out.println("Password corretta!");
            return true;
        } else {
            System.out.println("Password errata! Operazione annullata.");
            return false;
        }
    }
}
