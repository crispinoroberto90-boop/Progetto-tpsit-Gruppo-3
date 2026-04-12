// ============================================
// BAR MANAGER - JAVASCRIPT
// ============================================

// ============================================
// CONFIGURAZIONE API
// ============================================

const API_BASE_URL = "http://localhost:8080/api";

// ============================================
// DATI GLOBALI
// ============================================

const barData = {
    menu: [],
    ordini: [],
    pagamenti: [],
    dipendenti: [],
    inventario: {},
    carrello: [],
    ordineCorrente: null
};

// ============================================
// FUNZIONI DI UTILITÀ
// ============================================

function formatCurrency(amount) {
    return new Intl.NumberFormat('it-IT', {
        style: 'currency',
        currency: 'EUR'
    }).format(amount);
}

function switchTab(tabName) {
    // Nascondi tutti i tab content
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });

    // Rimuovi active dai bottoni
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });

    // Mostra il tab selezionato
    const tabElement = document.getElementById(tabName);
    if (tabElement) {
        tabElement.classList.add('active');
    }

    // Aggiungi active al bottone
    event.target.closest('.tab-btn').classList.add('active');
}

function toggleElement(elementId) {
    const element = document.getElementById(elementId);
    if (element) {
        element.classList.toggle('hidden');
    }
}

// ============================================
// MENU - FUNZIONI
// ============================================

async function loadMenu() {
    try {
        const response = await fetch(`${API_BASE_URL}/menu`);
        barData.menu = await response.json();
        
        const menuGrid = document.getElementById('menuGrid');
        menuGrid.innerHTML = '';

        barData.menu.forEach(item => {
            const card = document.createElement('div');
            card.className = 'menu-card';
            card.innerHTML = `
                <div class="category">${item.categoria}</div>
                <h4>${item.nome}</h4>
                <div class="price">${formatCurrency(item.prezzo)}</div>
                <div class="menu-card-actions">
                    <button class="btn-primary" onclick="addToCart({id: ${item.id}, nome: '${item.nome}', prezzo: ${item.prezzo}})">
                        + Aggiungi
                    </button>
                </div>
            `;
            menuGrid.appendChild(card);
        });

        loadProductSelect();
    } catch (error) {
        console.error('Errore caricamento menu:', error);
        showNotification('❌ Errore nel caricamento del menu', 'error');
    }
}

function loadProductSelect() {
    const select = document.getElementById('prodottoSelect');
    select.innerHTML = '<option value="">-- Seleziona un prodotto --</option>';
    
    barData.menu.forEach(item => {
        const option = document.createElement('option');
        option.value = item.id;
        option.textContent = `${item.nome} (${formatCurrency(item.prezzo)})`;
        select.appendChild(option);
    });
}

function toggleAddMenu() {
    const form = document.getElementById('addMenuForm');
    form.classList.toggle('hidden');
    if (!form.classList.contains('hidden')) {
        document.getElementById('menuNome').focus();
    }
}

function addMenuItem(event) {
    event.preventDefault();
    
    const nome = document.getElementById('menuNome').value;
    const prezzo = parseFloat(document.getElementById('menuPrezzo').value);

    if (nome && prezzo > 0) {
        fetch(`${API_BASE_URL}/menu`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nome: nome,
                prezzo: prezzo,
                categoria: "Custom"
            })
        })
        .then(response => response.json())
        .then(data => {
            document.getElementById('addMenuForm').reset();
            toggleAddMenu();
            loadMenu();
            showNotification(`✅ Voce "${nome}" aggiunta al menu!`, 'success');
        })
        .catch(error => {
            console.error('Errore:', error);
            showNotification('❌ Errore nell\'aggiunta della voce', 'error');
        });
    }
}

// ============================================
// ORDINI - FUNZIONI
// ============================================

async function loadOrders() {
    try {
        const response = await fetch(`${API_BASE_URL}/ordini`);
        barData.ordini = await response.json();
        
        const ordiniList = document.getElementById('ordiniList');
        ordiniList.innerHTML = '';

        if (barData.ordini.length === 0) {
            ordiniList.innerHTML = `
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <p>Nessun ordine registrato ancora</p>
                </div>
            `;
            return;
        }

        // Header
        const header = document.createElement('div');
        header.className = 'order-item header';
        header.innerHTML = `
            <div>ID Ordine</div>
            <div>Tipo</div>
            <div>Totale</div>
            <div>Ora</div>
        `;
        ordiniList.appendChild(header);

        // Orders
        barData.ordini.forEach(ordine => {
            const total = ordine.items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
            const dataOra = new Date(ordine.dataOra).toLocaleTimeString('it-IT', { hour: '2-digit', minute: '2-digit' });
            
            const row = document.createElement('div');
            row.className = 'order-item';
            row.innerHTML = `
                <div class="order-id">#${ordine.id}</div>
                <div>
                    <span class="order-type">${ordine.tipo === 'banco' ? '🏪 Banco' : '🪑 Tavolo'}</span>
                </div>
                <div class="order-amount">${formatCurrency(total)}</div>
                <div>${dataOra}</div>
            `;
            ordiniList.appendChild(row);
        });
    } catch (error) {
        console.error('Errore caricamento ordini:', error);
    }
}

function nuovoOrdine() {
    const form = document.getElementById('newOrderForm');
    form.classList.remove('hidden');
    barData.carrello = [];
    document.getElementById('newOrderForm').reset();
    document.getElementById('carritoSection').classList.add('hidden');
}

function creaOrdine(event) {
    event.preventDefault();

    const tipoOrdine = document.querySelector('input[name="tipoOrdine"]:checked');
    
    if (!tipoOrdine || barData.carrello.length === 0) {
        alert('⚠️ Seleziona tipo ordine e aggiungi articoli!');
        return;
    }

    // Crea ordine
    fetch(`${API_BASE_URL}/ordini`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            tipo: tipoOrdine.value
        })
    })
    .then(response => response.json())
    .then(ordine => {
        // Aggiungi items all'ordine
        let promiseChain = Promise.resolve();
        
        barData.carrello.forEach(item => {
            promiseChain = promiseChain.then(() => 
                fetch(`${API_BASE_URL}/ordini/${ordine.id}/items`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        id: item.id,
                        nome: item.nome,
                        price: item.prezzo,
                        quantity: item.quantita,
                        categoria: 'Custom'
                    })
                })
            );
        });

        promiseChain.then(() => {
            document.getElementById('newOrderForm').reset();
            document.getElementById('newOrderForm').classList.add('hidden');
            barData.carrello = [];
            document.getElementById('carritoSection').classList.add('hidden');
            
            loadOrders();
            updateHeaders();
            
            const total = barData.carrello.reduce((sum, item) => sum + (item.prezzo * item.quantita), 0);
            showNotification(`✅ Ordine #${ordine.id} registrato! Totale: ${formatCurrency(total)}`, 'success');
        });
    })
    .catch(error => {
        console.error('Errore:', error);
        showNotification('❌ Errore nella creazione dell\'ordine', 'error');
    });
}

function annullaOrdine() {
    document.getElementById('newOrderForm').reset();
    document.getElementById('newOrderForm').classList.add('hidden');
    barData.carrello = [];
    document.getElementById('carritoSection').classList.add('hidden');
}

function addToCart(item) {
    const quantita = parseInt(document.getElementById('quantita')?.value) || 1;
    
    // Apri la sezione ordini
    if (document.getElementById('newOrderForm').classList.contains('hidden')) {
        nuovoOrdine();
    }

    const existingItem = barData.carrello.find(c => c.id === item.id);
    
    if (existingItem) {
        existingItem.quantita += quantita;
    } else {
        barData.carrello.push({
            ...item,
            quantita: quantita
        });
    }

    updateCarrello();
}

function aggiungiAlOrdine() {
    const prodottoSelect = document.getElementById('prodottoSelect');
    const quantita = parseInt(document.getElementById('quantita').value) || 1;

    if (!prodottoSelect.value) {
        alert('⚠️ Seleziona un prodotto!');
        return;
    }

    const prodottoId = parseInt(prodottoSelect.value);
    const prodotto = barData.menu.find(m => m.id === prodottoId);

    if (prodotto) {
        addToCart(prodotto);
        document.getElementById('prodottoSelect').value = '';
        document.getElementById('quantita').value = '1';
    }
}

function updateCarrello() {
    const carrello = document.getElementById('carrito');
    const carritoSection = document.getElementById('carritoSection');
    
    if (barData.carrello.length === 0) {
        carritoSection.classList.add('hidden');
        return;
    }

    carritoSection.classList.remove('hidden');
    carrello.innerHTML = '';

    barData.carrello.forEach((item, index) => {
        const itemDiv = document.createElement('div');
        itemDiv.className = 'carrello-item';
        itemDiv.innerHTML = `
            <div class="carrello-item-info">
                <div class="carrello-item-name">${item.nome}</div>
                <div class="carrello-item-qty">Qty: ${item.quantita} × ${formatCurrency(item.prezzo)}</div>
            </div>
            <div class="carrello-item-price">${formatCurrency(item.prezzo * item.quantita)}</div>
            <button class="btn-danger" onclick="removeFromCart(${index})">✕</button>
        `;
        carrello.appendChild(itemDiv);
    });

    const totale = barData.carrello.reduce((sum, item) => sum + (item.prezzo * item.quantita), 0);
    document.getElementById('totaleCarrello').textContent = formatCurrency(totale);
}

function removeFromCart(index) {
    barData.carrello.splice(index, 1);
    updateCarrello();
}

// ============================================
// PAGAMENTI - FUNZIONI
// ============================================

function elaboraPagamento(event) {
    event.preventDefault();

    const ordineId = parseInt(document.getElementById('ordineIdPagamento').value);
    const importo = parseFloat(document.getElementById('importoPagamento').value);
    const metodo = document.getElementById('metodoPagamento').value;

    fetch(`${API_BASE_URL}/pagamenti`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            ordineId: ordineId,
            importo: importo,
            metodo: metodo
        })
    })
    .then(response => response.json())
    .then(data => {
        event.target.reset();
        updatePaymentStats();
        updateHeaders();
        showNotification(`✅ Pagamento di ${formatCurrency(importo)} elaborato!`, 'success');
    })
    .catch(error => {
        console.error('Errore:', error);
        showNotification('❌ Errore nell\'elaborazione del pagamento', 'error');
    });
}

function updatePaymentStats() {
    fetch(`${API_BASE_URL}/pagamenti/stats`)
        .then(response => response.json())
        .then(stats => {
            document.getElementById('totalContanti').textContent = formatCurrency(stats.totalCash);
            document.getElementById('totalCarta').textContent = formatCurrency(stats.totalCard);
            document.getElementById('numPagamenti').textContent = stats.paymentCount;
            document.getElementById('incassoTotale').textContent = formatCurrency(stats.totalRevenue);
        })
        .catch(error => console.error('Errore:', error));
}

// ============================================
// DIPENDENTI - FUNZIONI
// ============================================

async function loadDipendenti() {
    try {
        const response = await fetch(`${API_BASE_URL}/dipendenti`);
        barData.dipendenti = await response.json();
        
        const container = document.getElementById('dipendentiContainer');
        container.innerHTML = '';

        if (barData.dipendenti.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <div class="empty-state-icon">👥</div>
                    <p>Nessun dipendente registrato</p>
                </div>
            `;
            return;
        }

        barData.dipendenti.forEach(dip => {
            const card = document.createElement('div');
            card.className = 'dipendente-card';
            
            const turnoEmoji = dip.turno === 'mattina' ? '🌅' : dip.turno === 'pomeriggio' ? '☀️' : '🌙';
            const turnoLabel = dip.turno === 'mattina' ? 'Mattina (06:00-14:00)' : 
                              dip.turno === 'pomeriggio' ? 'Pomeriggio (14:00-22:00)' : 'Sera (22:00-06:00)';
            
            card.innerHTML = `
                <h4>${turnoEmoji} ${dip.nome}</h4>
                <div class="dipendente-turno">${turnoLabel}</div>
                <div class="dipendente-status">👤 ID: #${dip.id}</div>
            `;
            container.appendChild(card);
        });
    } catch (error) {
        console.error('Errore caricamento dipendenti:', error);
    }
}

function toggleAddDipendente() {
    const form = document.getElementById('addDipForm');
    form.classList.toggle('hidden');
}

function addDipendente(event) {
    event.preventDefault();

    const nome = document.getElementById('dipNome').value;
    const turno = document.getElementById('dipTurno').value;

    if (nome && turno) {
        fetch(`${API_BASE_URL}/dipendenti`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nome: nome,
                turno: turno
            })
        })
        .then(response => response.json())
        .then(data => {
            document.getElementById('addDipForm').reset();
            toggleAddDipendente();
            loadDipendenti();
            showNotification(`✅ Dipendente "${nome}" aggiunto!`, 'success');
        })
        .catch(error => {
            console.error('Errore:', error);
            showNotification('❌ Errore nell\'aggiunta del dipendente', 'error');
        });
    }
}

// ============================================
// REPORT - FUNZIONI
// ============================================

function generaReportVendite() {
    const reportContent = document.getElementById('reportContent');

    fetch(`${API_BASE_URL}/ordini`)
        .then(response => response.json())
        .then(ordini => {
            let html = '<h3>📈 Report Vendite</h3>';

            if (ordini.length === 0) {
                html += '<p>Nessun ordine registrato</p>';
                reportContent.innerHTML = html;
                return;
            }

            // Conteggio prodotti venduti
            const vendite = {};
            ordini.forEach(ordine => {
                ordine.items.forEach(item => {
                    if (!vendite[item.nome]) {
                        vendite[item.nome] = { quantita: 0, ricavo: 0 };
                    }
                    vendite[item.nome].quantita += item.quantity;
                    vendite[item.nome].ricavo += item.price * item.quantity;
                });
            });

            html += '<table class="table"><thead><tr><th>Prodotto</th><th>Quantità</th><th>Ricavo</th></tr></thead><tbody>';
            
            Object.entries(vendite).forEach(([prodotto, data]) => {
                html += `
                    <tr>
                        <td>${prodotto}</td>
                        <td>${data.quantita}</td>
                        <td>${formatCurrency(data.ricavo)}</td>
                    </tr>
                `;
            });

            html += '</tbody></table>';
            reportContent.innerHTML = html;

            // Aggiorna top products
            updateTopProductsChart(vendite);
        })
        .catch(error => console.error('Errore:', error));
}

function generaReportIncassi() {
    const reportContent = document.getElementById('reportContent');

    fetch(`${API_BASE_URL}/pagamenti`)
        .then(response => response.json())
        .then(pagamenti => {
            let html = '<h3>💰 Report Incassi</h3>';

            if (pagamenti.length === 0) {
                html += '<p>Nessun pagamento registrato</p>';
                reportContent.innerHTML = html;
                return;
            }

            let totalContanti = 0, totalCarta = 0, totalAltri = 0;

            pagamenti.forEach(p => {
                if (p.method === 'contanti') totalContanti += p.amount;
                else if (p.method === 'carta' || p.method === 'bancomat') totalCarta += p.amount;
                else totalAltri += p.amount;
            });

            const totaleGenerale = totalContanti + totalCarta + totalAltri;

            html += `
                <div class="stats-grid">
                    <div class="stat-box">
                        <h4>Contanti</h4>
                        <p class="stat-number">${formatCurrency(totalContanti)}</p>
                    </div>
                    <div class="stat-box">
                        <h4>Carta/Bancomat</h4>
                        <p class="stat-number">${formatCurrency(totalCarta)}</p>
                    </div>
                    <div class="stat-box">
                        <h4>Altro</h4>
                        <p class="stat-number">${formatCurrency(totalAltri)}</p>
                    </div>
                    <div class="stat-box">
                        <h4>Totale Incasso</h4>
                        <p class="stat-number highlight">${formatCurrency(totaleGenerale)}</p>
                    </div>
                </div>

                <table class="table" style="margin-top: 20px;">
                    <thead>
                        <tr>
                            <th>Ordine ID</th>
                            <th>Importo</th>
                            <th>Metodo</th>
                            <th>Ora</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${pagamenti.map(p => `
                            <tr>
                                <td>#${p.orderId}</td>
                                <td>${formatCurrency(p.amount)}</td>
                                <td>${p.method}</td>
                                <td>${new Date(p.data).toLocaleTimeString('it-IT', { hour: '2-digit', minute: '2-digit' })}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;

            reportContent.innerHTML = html;
        })
        .catch(error => console.error('Errore:', error));
}

function generaReportInventario() {
    const reportContent = document.getElementById('reportContent');

    fetch(`${API_BASE_URL}/inventario`)
        .then(response => response.json())
        .then(inventario => {
            let html = '<h3>📦 Report Inventario</h3><table class="table"><thead><tr><th>Prodotto</th><th>Quantità</th><th>Stato</th></tr></thead><tbody>';

            Object.entries(inventario).forEach(([prodotto, quantita]) => {
                let status = '✅ OK';
                let statusClass = 'status-ok';
                
                if (quantita <= 10) {
                    status = '⚠️ Basso';
                    statusClass = 'status-low';
                } else if (quantita <= 25) {
                    status = '⚠️ Medio';
                    statusClass = 'status-medium';
                }

                html += `<tr><td>${prodotto}</td><td>${quantita}</td><td class="${statusClass}">${status}</td></tr>`;
            });

            html += '</tbody></table>';
            reportContent.innerHTML = html;
        })
        .catch(error => console.error('Errore:', error));
}

function updateTopProductsChart(vendite) {
    const topProducts = document.getElementById('topProducts');
    topProducts.innerHTML = '';

    const sorted = Object.entries(vendite)
        .sort((a, b) => b[1].quantita - a[1].quantita)
        .slice(0, 5);

    const maxQty = Math.max(...sorted.map(s => s[1].quantita));

    sorted.forEach(([prodotto, data]) => {
        const percentage = (data.quantita / maxQty) * 100;
        const item = document.createElement('div');
        item.className = 'chart-item';
        item.innerHTML = `
            <div class="chart-label">${prodotto}</div>
            <div class="chart-bar" style="width: ${percentage}%">${data.quantita}</div>
        `;
        topProducts.appendChild(item);
    });
}

// ============================================
// INVENTARIO - FUNZIONI
// ============================================

async function loadInventario() {
    try {
        const response = await fetch(`${API_BASE_URL}/inventario`);
        barData.inventario = await response.json();
        
        const tbody = document.getElementById('inventarioBody');
        tbody.innerHTML = '';

        Object.entries(barData.inventario).forEach(([prodotto, quantita]) => {
            let status = '✅ OK';
            let statusClass = 'status-ok';
            
            if (quantita <= 10) {
                status = '⚠️ Basso';
                statusClass = 'status-low';
            } else if (quantita <= 25) {
                status = '⚠️ Medio';
                statusClass = 'status-medium';
            }

            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong>${prodotto}</strong></td>
                <td>${quantita}</td>
                <td><span class="${statusClass}">${status}</span></td>
                <td>
                    <button class="btn-primary" onclick="updateInventarioBtn('${prodotto}', 10)">+10</button>
                    <button class="btn-secondary" onclick="updateInventarioBtn('${prodotto}', -5)">-5</button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Errore caricamento inventario:', error);
    }
}

function toggleAddInventario() {
    const form = document.getElementById('addInventarioForm');
    form.classList.toggle('hidden');
}

function addInventario(event) {
    event.preventDefault();

    const prodotto = document.getElementById('invProdotto').value;
    const quantita = parseInt(document.getElementById('invQuantita').value);

    if (prodotto && quantita > 0) {
        fetch(`${API_BASE_URL}/inventario/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                prodotto: prodotto,
                quantita: quantita
            })
        })
        .then(response => response.json())
        .then(data => {
            document.getElementById('addInventarioForm').reset();
            toggleAddInventario();
            loadInventario();
            showNotification(`✅ Scorte aggiunte: ${prodotto} +${quantita}`, 'success');
        })
        .catch(error => {
            console.error('Errore:', error);
            showNotification('❌ Errore nell\'aggiunta delle scorte', 'error');
        });
    }
}

function updateInventarioBtn(prodotto, change) {
    if (change > 0) {
        fetch(`${API_BASE_URL}/inventario/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                prodotto: prodotto,
                quantita: change
            })
        })
        .then(() => loadInventario())
        .catch(error => console.error('Errore:', error));
    } else {
        fetch(`${API_BASE_URL}/inventario/remove`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                prodotto: prodotto,
                quantita: Math.abs(change)
            })
        })
        .then(() => loadInventario())
        .catch(error => console.error('Errore:', error));
    }
}

// ============================================
// HEADERS E STATISTICHE
// ============================================

async function updateHeaders() {
    try {
        // Conteggio ordini
        const ordiniResponse = await fetch(`${API_BASE_URL}/ordini`);
        const ordini = await ordiniResponse.json();
        document.getElementById('ordiniCount').textContent = ordini.length;

        // Totale incasso
        const statsResponse = await fetch(`${API_BASE_URL}/pagamenti/stats`);
        const stats = await statsResponse.json();
        document.getElementById('incassoTotal').textContent = formatCurrency(stats.totalRevenue);
    } catch (error) {
        console.error('Errore aggiornamento headers:', error);
    }
}

// ============================================
// NOTIFICHE
// ============================================

function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 25px;
        background-color: ${type === 'success' ? '#27AE60' : '#3498DB'};
        color: white;
        border-radius: 6px;
        box-shadow: 0 4px 6px rgba(0,0,0,0.3);
        z-index: 9999;
        animation: slideIn 0.3s ease;
    `;
    
    notification.textContent = message;
    document.body.appendChild(notification);

    setTimeout(() => {
        notification.remove();
    }, 3000);
}

// ============================================
// INIT
// ============================================

function initializePage() {
    loadMenu();
    loadDipendenti();
    loadOrders();
    loadInventario();
    updateHeaders();
    updatePaymentStats();
}

// Carica dati quando il DOM è pronto
document.addEventListener('DOMContentLoaded', initializePage);

// Aggiungi stile di animazione
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
`;
document.head.appendChild(style);
