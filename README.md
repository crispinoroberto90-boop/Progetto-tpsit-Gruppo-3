# Progetto-tpsit-Gruppo-3
Progetto infradisciplinare informatica/tipst

# 📊 Sistema Gestionale Bar - Documentazione Completa

## 📌 Descrizione

Questo progetto rappresenta un **sistema gestionale per un bar**, sviluppato in Java.
La classe principale `Bar` gestisce tutte le funzionalità fondamentali:

* Ordini
* Pagamenti
* Menu
* Magazzino
* Personale
* Report
* Autenticazione
* Backup

Il codice segue i requisiti funzionali (RF) e non funzionali (RNF) tipici di un SRS (Software Requirements Specification).

---

# 🔹 Import: perché sono necessari

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
```

Questi import servono per utilizzare strutture dati standard di Java:

* **ArrayList** → lista dinamica ordinata
* **List** → interfaccia generale per liste
* **HashMap** → struttura dati chiave-valore molto veloce
* **Map** → interfaccia generale per mappe

👉 Scopo: gestire collezioni di dati in modo efficiente e flessibile.

---

# 🔹 Struttura della classe

```java
public class Bar {
```

Questa è la **classe principale**, che rappresenta l’intero sistema del bar.

---

# 🔹 Attributi: cosa rappresentano

## 🍽️ Menu

```java
private List<MenuItem> menu;
```

Contiene tutti i prodotti venduti (es. caffè, panini).

---

## 📦 Magazzino

```java
private Map<String, Integer> inventory;
```

Mappa:

* **chiave** → nome prodotto
* **valore** → quantità disponibile

👉 Serve per monitorare le scorte.

---

## 👨‍💼 Personale

```java
private List<Employee> employees;
```

Lista dei dipendenti del bar.

---

## 🧾 Ordini

```java
private List<Order> orders;
```

Contiene tutti gli ordini effettuati.

---

## 💳 Pagamenti

```java
private PaymentSystem paymentSystem;
```

Gestisce l’elaborazione dei pagamenti.

---

## 📊 Report

```java
private ReportGenerator reportGenerator;
```

Genera report su vendite e incassi.

---

## 🔐 Sicurezza

```java
private Map<String, String> userCredentials;
```

Memorizza username e password.

⚠️ Nota: in un sistema reale si usano password cifrate (hash).

---

## 💾 Backup

```java
private boolean backupEnabled;
```

Indica se il backup automatico è attivo.

---

# 🔹 Costruttore: inizializzazione sistema

```java
public Bar() {
```

Viene eseguito quando crei un oggetto `Bar`.

### Inizializza tutte le strutture:

```java
this.menu = new ArrayList<>();
this.inventory = new HashMap<>();
this.employees = new ArrayList<>();
this.orders = new ArrayList<>();
```

👉 Scopo: evitare errori (null pointer) e preparare il sistema.

---

### Inizializza i servizi:

```java
this.paymentSystem = new PaymentSystem();
this.reportGenerator = new ReportGenerator();
```

👉 Divide le responsabilità (buona progettazione).

---

### Imposta sicurezza e backup:

```java
this.userCredentials = new HashMap<>();
this.backupEnabled = true;
```

---

# 🔹 Gestione ordini (RF1)

```java
public void registerOrder(Order order)
```

### Cosa fa:

1. Salva l’ordine
2. Aggiorna il magazzino

```java
orders.add(order);
updateInventory(order);
```

👉 Ogni ordine riduce automaticamente le scorte.

---

# 🔹 Pagamenti (RF2)

```java
public boolean processPayment(Payment payment)
```

Delegazione:

```java
return paymentSystem.process(payment);
```

👉 Il sistema è modulare: il pagamento è gestito da una classe separata.

---

# 🔹 Gestione menu (RF3)

### Aggiunta prodotto

```java
menu.add(item);
```

### Rimozione prodotto

```java
menu.remove(item);
```

👉 Permette aggiornamenti dinamici del menu.

---

# 🔹 Magazzino (RF4)

## Aggiornamento automatico

```java
for (OrderItem item : order.getItems())
```

Scorre ogni prodotto ordinato.

---

### Logica:

```java
inventory.put(product, currentStock - quantity);
```

👉 Riduce le scorte.

---

### Controllo scorte basse:

```java
if (inventory.get(product) < 10)
```

👉 Avvisa quando un prodotto sta per finire.

---

## Aggiunta manuale scorte

```java
inventory.getOrDefault(product, 0)
```

👉 Se il prodotto non esiste, parte da 0.

---

# 🔹 Gestione personale (RF5)

### Aggiungere dipendente

```java
employees.add(employee);
```

### Rimuovere dipendente

```java
employees.remove(employee);
```

---

### Assegnare turno

```java
employee.setShift(shift);
```

👉 Associa un turno (es. mattina, sera).

---

# 🔹 Report (RF6)

```java
reportGenerator.generateSalesReport(orders);
```

👉 Analizza gli ordini per creare report.

---

# 🔹 Autenticazione (RNF2)

```java
userCredentials.containsKey(username)
```

Verifica esistenza utente.

```java
userCredentials.get(username).equals(password)
```

Verifica password.

👉 Sistema semplice di login.

---

# 🔹 Backup

```java
if (backupEnabled)
```

👉 Controlla se il backup è attivo.

```java
System.out.println("Backup eseguito.");
```

---

# 🔹 Scalabilità (RNF4)

```java
public void addBranch(Bar branch)
```

👉 Permette di gestire più punti vendita (non implementato completamente).

---

# 🔹 Getter

Permettono accesso controllato ai dati:

```java
getMenu()
getInventory()
getEmployees()
getOrders()
```

👉 Principio di incapsulamento.

---

# 🔹 Classi interne

## MenuItem

Rappresenta un prodotto:

* nome
* prezzo

---

## Employee

Rappresenta un dipendente:

* nome
* turno

---

## Order

Contiene:

* lista prodotti
* tipo ordine (banco/tavolo)

---

## OrderItem

Rappresenta un singolo prodotto ordinato:

* nome
* quantità

---

## Payment

Contiene:

* importo
* metodo (contanti/digitale)

---

# 🔹 Classi di supporto

## PaymentSystem

Gestisce i pagamenti (simulato).

---

## ReportGenerator

Genera report (simulato).

---

# ✅ Conclusione

Questo sistema implementa un **gestionale completo per un bar**, con:

✔ gestione ordini
✔ aggiornamento magazzino automatico
✔ gestione personale
✔ pagamenti modulari
✔ reportistica
✔ autenticazione utenti
✔ supporto backup

---

# 🚀 Possibili miglioramenti

* Database al posto delle liste
* Password cifrate (hash)
* Interfaccia grafica
* API REST (Spring Boot)
* Gestione errori avanzata
* Architettura MVC

---

