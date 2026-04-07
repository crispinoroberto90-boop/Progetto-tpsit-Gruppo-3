import java.util.ArrayList;
import java.util.List;

// Classe con il menu del giorno hardcoded
// Facilissimo da modificare: aggiungi/togli voci come vuoi!
public class MenuGiorno {
    
    // Metodo statico che ritorna il menu del giorno con tutti gli item
    public static List<MenuItem> getMenuDelGiorno() {
        List<MenuItem> menu = new ArrayList<>();
        
        // BEVANDE
        menu.add(new MenuItem("Caffe", 1.50));
        menu.add(new MenuItem("Cappuccino", 2.00));
        menu.add(new MenuItem("Espresso", 1.00));
        menu.add(new MenuItem("Latte", 2.50));
        menu.add(new MenuItem("Acqua", 0.50));
        menu.add(new MenuItem("Bibita", 2.00));
        
        // PANINI
        menu.add(new MenuItem("Panino Prosciutto", 4.00));
        menu.add(new MenuItem("Panino Formaggio", 3.50));
        menu.add(new MenuItem("Panino Mortadella", 3.80));
        menu.add(new MenuItem("Panino Pollo", 4.50));
        
        // DOLCI
        menu.add(new MenuItem("Cornetto", 1.20));
        menu.add(new MenuItem("Brioche", 1.50));
        menu.add(new MenuItem("Torta", 3.00));
        menu.add(new MenuItem("Biscotti", 1.00));
        
        // SNACK
        menu.add(new MenuItem("Pizza al taglio", 2.50));
        menu.add(new MenuItem("Focaccia", 2.00));
        menu.add(new MenuItem("Taralli", 1.50));
        
        return menu;
    }
    
    // Se vuoi vedere il menu stampato
    public static void stampaMenuDelGiorno() {
        System.out.println("\n========== MENU DEL GIORNO ==========");
        List<MenuItem> menu = getMenuDelGiorno();
        
        System.out.println("BEVANDE:");
        System.out.println("- Caffe: 1.50€");
        System.out.println("- Cappuccino: 2.00€");
        System.out.println("- Espresso: 1.00€");
        System.out.println("- Latte: 2.50€");
        System.out.println("- Acqua: 0.50€");
        System.out.println("- Bibita: 2.00€");
        
        System.out.println("\nPANINI:");
        System.out.println("- Panino Prosciutto: 4.00€");
        System.out.println("- Panino Formaggio: 3.50€");
        System.out.println("- Panino Mortadella: 3.80€");
        System.out.println("- Panino Pollo: 4.50€");
        
        System.out.println("\nDOLCI:");
        System.out.println("- Cornetto: 1.20€");
        System.out.println("- Brioche: 1.50€");
        System.out.println("- Torta: 3.00€");
        System.out.println("- Biscotti: 1.00€");
        
        System.out.println("\nSNACK:");
        System.out.println("- Pizza al taglio: 2.50€");
        System.out.println("- Focaccia: 2.00€");
        System.out.println("- Taralli: 1.50€");
        
        System.out.println("=====================================\n");
    }
}