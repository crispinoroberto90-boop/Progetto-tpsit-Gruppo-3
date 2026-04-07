// Sistema per elaborare i pagamenti
public class PaymentSystem {
    public boolean elabora(Payment pagamento) {
        // Logica semplificata per processare pagamento
        System.out.println("Pagamento di " + pagamento.getImporto() + " elaborato con metodo " + pagamento.getMetodo());
        return true;
    }
}