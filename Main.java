// File di prova per testare il sistema Bar con menu interattivo
// Tipo, scegli cosa fare e vediamo se funziona

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
            System.out.println("13. Mostra ordini");
            System.out.println("14. Mostra inventario");
            System.out.println("0. Esci");

            System.out.print("Scegli: ");
            int scelta = scanner.nextInt();
            scanner.nextLine(); // Per il newline

            switch (scelta) {
                case 1:
                    System.out.print("Nome voce: ");
                    String nomeVoce = scanner.nextLine();
                    System.out.print("Prezzo: ");
                    double prezzo = scanner.nextDouble();
                    bar.aggiungiVoceMenu(new MenuItem(nomeVoce, prezzo));
                    System.out.println("Voce aggiunta!");
                    break;
                case 2:
                    System.out.print("Nome voce da rimuovere: ");
                    String nomeRimuovi = scanner.nextLine();
                    // Trova e rimuovi (semplificato)
                    bar.getVociMenu().removeIf(v -> v.getNome().equals(nomeRimuovi));
                    System.out.println("Voce rimossa!");
                    break;
                case 3:
                    System.out.print("Nome dipendente: ");
                    String nomeDip = scanner.nextLine();
                    Dipendente dip = new Dipendente(nomeDip);
                    bar.aggiungiDipendente(dip);
                    System.out.print("Turno (es. mattina): ");
                    String turno = scanner.nextLine();
                    bar.assegnaTurno(dip, turno);
                    System.out.println("Dipendente aggiunto!");
                    break;
                case 4:
                    System.out.print("Tipo ordine (banco/tavolo): ");
                    String tipo = scanner.nextLine();
                    ordineCorrente = new Order(tipo);
                    System.out.println("Ordine creato!");
                    break;
                case 5:
                    if (ordineCorrente == null) {
                        System.out.println("Prima crea un ordine!");
                        break;
                    }
                    System.out.print("Nome prodotto: ");
                    String prod = scanner.nextLine();
                    System.out.print("Quantita: ");
                    int quant = scanner.nextInt();
                    ordineCorrente.aggiungiElemento(new OrderItem(prod, quant));
                    System.out.println("Elemento aggiunto!");
                    break;
                case 6:
                    if (ordineCorrente == null) {
                        System.out.println("Nessun ordine corrente!");
                        break;
                    }
                    bar.registraOrdine(ordineCorrente);
                    System.out.println("Ordine registrato!");
                    ordineCorrente = null;
                    break;
                case 7:
                    System.out.print("Importo: ");
                    double imp = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Metodo (contanti/digitale): ");
                    String met = scanner.nextLine();
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
                    System.out.print("Prodotto: ");
                    String prodInv = scanner.nextLine();
                    System.out.print("Quantita: ");
                    int quantInv = scanner.nextInt();
                    bar.aggiungiInventario(prodInv, quantInv);
                    System.out.println("Scorte aggiunte!");
                    break;
                case 11:
                    System.out.println("Menu:");
                    for (MenuItem v : bar.getVociMenu()) {
                        System.out.println("- " + v.getNome() + ": " + v.getPrezzo() + "€");
                    }
                    break;
                case 12:
                    System.out.println("Dipendenti:");
                    for (Dipendente d : bar.getDipendenti()) {
                        System.out.println("- " + d.getNome() + " (" + d.getTurno() + ")");
                    }
                    break;
                case 13:
                    System.out.println("Ordini: " + bar.getOrdini().size());
                    break;
                case 14:
                    System.out.println("Inventario:");
                    bar.getInventario().forEach((p, q) -> System.out.println("- " + p + ": " + q));
                    break;
                case 0:
                    System.out.println("Ciao!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Scelta non valida!");
            }
        }
    }
}