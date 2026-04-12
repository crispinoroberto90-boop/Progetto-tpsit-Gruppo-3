# 🍺 Bar Manager - Guida Avvio

## 📋 Struttura Progetto

```
Progetto-tpsit-Gruppo-3/
├── pom.xml                          # Dipendenze Maven
├── index.html                       # Frontend (interfaccia grafica)
├── style.css                        # Stili CSS
├── script.js                        # Logica JavaScript (comunicazione API)
│
├── src/main/java/com/bar/
│   ├── BarApplication.java          # Main Spring Boot
│   ├── controller/
│   │   ├── MenuController.java      # API Menu
│   │   ├── OrderController.java     # API Ordini
│   │   ├── PaymentController.java   # API Pagamenti
│   │   ├── EmployeeController.java  # API Dipendenti
│   │   └── InventoryController.java # API Inventario
│   ├── service/
│   │   ├── MenuService.java         # Logica Menu
│   │   ├── OrderService.java        # Logica Ordini
│   │   ├── PaymentService.java      # Logica Pagamenti
│   │   ├── EmployeeService.java     # Logica Dipendenti
│   │   └── InventoryService.java    # Logica Inventario
│   └── model/
│       ├── MenuItem.java
│       ├── Order.java
│       ├── OrderItem.java
│       ├── Payment.java
│       └── Employee.java
│
└── src/main/resources/
    └── application.properties       # Configurazione Spring Boot
```

## 🚀 Prerequisiti

- **Java 11+** installato
- **Maven 3.6+** installato
- **Browser moderno** (Chrome, Firefox, Edge, Safari)

### Verificare installazione

```bash
java -version
mvn -version
```

## 🔧 Setup Iniziale

### 1. Naviga nella cartella del progetto

```bash
cd "c:\Users\Alessio\Documents\GitHub\Progetto-tpsit-Gruppo-3"
```

### 2. Compila il progetto Maven

```bash
mvn clean install
```

Questo scaricherà tutte le dipendenze (potrebbe richiedere qualche minuto la prima volta).

### 3. Avvia il server Spring Boot

```bash
mvn spring-boot:run
```

**Output atteso:**
```
2026-04-12 10:30:00 INFO  - Tomcat started on port(s): 8080 (http)
2026-04-12 10:30:00 INFO  - Bar Manager started successfully
```

## 🌐 Accedi all'Interfaccia

1. **Il server deve essere in esecuzione** (vedi passo 3)
2. Apri il browser e vai a: **http://localhost:8080**
3. Oppure apri direttamente il file `index.html` (nota: senza server alcuni dati potrebbero non caricarsi correttamente)

## 📋 API Disponibili

Tutte le API sono raggiungibili su `http://localhost:8080/api`

### 🍽️ Menu
- `GET /api/menu` - Ottiene tutti gli item del menu
- `POST /api/menu` - Aggiunge un nuovo item
- `DELETE /api/menu/{id}` - Elimina un item

### 🛵 Ordini
- `GET /api/ordini` - Ottiene tutti gli ordini
- `GET /api/ordini/{id}` - Ottiene un ordine specifico
- `POST /api/ordini` - Crea un nuovo ordine
- `POST /api/ordini/{id}/items` - Aggiunge item a ordine
- `GET /api/ordini/{id}/total` - Calcola totale ordine

### 💳 Pagamenti
- `GET /api/pagamenti` - Ottiene tutti i pagamenti
- `POST /api/pagamenti` - Elabora un nuovo pagamento
- `GET /api/pagamenti/stats` - Statistiche pagamenti

### 👥 Dipendenti
- `GET /api/dipendenti` - Ottiene tutti i dipendenti
- `POST /api/dipendenti` - Aggiunge un dipendente
- `DELETE /api/dipendenti/{id}` - Elimina dipendente

### 📦 Inventario
- `GET /api/inventario` - Ottiene inventario completo
- `GET /api/inventario/{product}` - Stock di un prodotto
- `POST /api/inventario/add` - Aggiunge scorte
- `POST /api/inventario/remove` - Rimuove scorte

## 🎨 Funzionalità dell'Interfaccia

### 💾 Gestione Menu
- Visualizza tutti i prodotti con categorie
- Aggiungi nuove voci al menu
- Organizzato per categorie (Bevande, Panini, Dolci, Snack)

### 🛒 Gestione Ordini
- Crea ordini (Banco/Tavolo)
- Aggiungi prodotti al carrello
- Calcolo automatico totale
- Visualizza storico ordini

### 💰 Elaborazione Pagamenti
- Registra pagamenti con diversi metodi
- Statistiche incassi in tempo reale
- Conteggio per tipo di pagamento

### 👨‍💼 Gestione Dipendenti
- Registrazione dipendenti con turni
- Organizzazione per turno (Mattina/Pomeriggio/Sera)

### 📊 Report e Statistiche
- **Report Vendite**: Prodotti più venduti e ricavi
- **Report Incassi**: Analisi pagamenti e totali
- **Report Inventario**: Stock e alert per prodotti in esaurimento

### 📦 Gestione Inventario
- Visualizza stock tutti prodotti
- Aggiungi/Rimuovi scorte
- Alert automatici per stock basso

## 🐛 Troubleshooting

### ❌ Errore "Connection refused"
**Causa**: Server non è in esecuzione
**Soluzione**: Assicurati che il comando `mvn spring-boot:run` sia ancora in esecuzione nel terminale

### ❌ Errore "Port 8080 already in use"
**Causa**: Un'altra applicazione usa la porta 8080
**Soluzione**: Modifica in `application.properties`:
```properties
server.port=8081
```

### ❌ CORS Error nel browser
**Causa**: Configurazione CORS non corretta
**Soluzione**: Già configurato in `BarApplication.java`, se persiste riavvia il server

### ❌ Maven non trovato
**Causa**: Maven non è installato o non è nel PATH
**Soluzione**: Installa Maven da https://maven.apache.org/download.cgi

## 📝 Modifica dei Dati

I dati sono memorizzati **in memoria** (NON in database). Al riavvio del server tutti i dati vengono resettati.

### Per usare un database persistente (optional):
Modifica `pom.xml` e aggiungi una dipendenza per database (es. MySQL, PostgreSQL)

## 🎯 Prossimi Passi

1. ✅ Compila con Maven
2. ✅ Avvia con `mvn spring-boot:run`
3. ✅ Apri `http://localhost:8080`
4. ✅ Inizia a usare l'app!

## 📞 Supporto

Se hai problemi:
1. Verifica che Java e Maven siano installati
2. Controlla che il server sia in esecuzione (vedi output in terminale)
3. Riavvia il server
4. Pulisci la cache: `mvn clean install`

---

**Creato il**: 12/04/2026  
**Versione**: 1.0.0  
**Tema**: Bar Management System with Spring Boot & REST API
