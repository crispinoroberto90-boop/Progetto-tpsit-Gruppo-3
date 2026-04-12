const API_BASE_URL = "http://localhost:8080/api";

let menuItems = [];
let cart = [];

function formatCurrency(amount) {
    return new Intl.NumberFormat('it-IT', {
        style: 'currency',
        currency: 'EUR'
    }).format(amount);
}

async function loadMenuForUser() {
    try {
        const response = await fetch(`${API_BASE_URL}/menu`);
        menuItems = await response.json();
        document.getElementById('menuCount').textContent = menuItems.length;

        const menuGrid = document.getElementById('menuGrid');
        menuGrid.innerHTML = '';

        if (menuItems.length === 0) {
            menuGrid.innerHTML = `
                <div class="empty-state" style="width:100%; padding: 40px; text-align:center;">
                    <div class="empty-state-icon">😔</div>
                    <p>Il menu è vuoto. Attendi che il manager aggiunga i prodotti.</p>
                </div>
            `;
            return;
        }

        menuItems.forEach(item => {
            const card = document.createElement('div');
            card.className = 'menu-card';
            card.innerHTML = `
                <div class="category">${item.categoria}</div>
                <h4>${item.nome}</h4>
                <div class="price">${formatCurrency(item.prezzo)}</div>
                <div class="menu-card-actions">
                    <button class="btn-primary" onclick="addToCart(${item.id})">Aggiungi</button>
                </div>
            `;
            menuGrid.appendChild(card);
        });
    } catch (error) {
        console.error('Errore caricamento menu utente:', error);
        showNotification('❌ Impossibile caricare il menu', 'error');
    }
}

function addToCart(itemId) {
    const item = menuItems.find(p => p.id === itemId);
    if (!item) return;

    const existing = cart.find(c => c.id === itemId);
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({
            id: item.id,
            nome: item.nome,
            prezzo: item.prezzo,
            quantity: 1,
            categoria: item.categoria
        });
    }

    renderCart();
}

function renderCart() {
    const cartItems = document.getElementById('cartItems');
    cartItems.innerHTML = '';

    if (cart.length === 0) {
        cartItems.innerHTML = `
            <div class="empty-state" style="padding: 25px;">
                <div class="empty-state-icon">🛒</div>
                <p>Il carrello è vuoto. Aggiungi prima un prodotto.</p>
            </div>
        `;
        updateCartTotal();
        return;
    }

    cart.forEach((item, index) => {
        const row = document.createElement('div');
        row.className = 'carrello-item';
        row.innerHTML = `
            <div class="carrello-item-info">
                <div class="carrello-item-name">${item.nome}</div>
                <div class="carrello-item-qty">${item.quantity} × ${formatCurrency(item.prezzo)}</div>
            </div>
            <div class="carrello-item-price">${formatCurrency(item.quantity * item.prezzo)}</div>
            <div>
                <button class="btn-secondary" onclick="updateQuantity(${index}, -1)">-</button>
                <button class="btn-primary" onclick="updateQuantity(${index}, 1)">+</button>
                <button class="btn-danger" onclick="removeItem(${index})">✕</button>
            </div>
        `;
        cartItems.appendChild(row);
    });

    updateCartTotal();
}

function updateQuantity(index, change) {
    const item = cart[index];
    if (!item) return;
    item.quantity += change;
    if (item.quantity <= 0) {
        cart.splice(index, 1);
    }
    renderCart();
}

function removeItem(index) {
    cart.splice(index, 1);
    renderCart();
}

function clearCart() {
    cart = [];
    renderCart();
}

function updateCartTotal() {
    const total = cart.reduce((sum, item) => sum + item.prezzo * item.quantity, 0);
    document.getElementById('cartTotal').textContent = formatCurrency(total);
    document.getElementById('cartTotalValue').textContent = formatCurrency(total);
}

async function placeOrder() {
    if (cart.length === 0) {
        showNotification('⚠️ Aggiungi almeno un prodotto al carrello', 'warning');
        return;
    }

    const tipo = document.getElementById('orderTypeSelect').value;
    const table = document.getElementById('tableNumber').value;

    if (tipo === 'tavolo' && (!table || parseInt(table) < 1)) {
        showNotification('⚠️ Inserisci un numero tavolo valido', 'warning');
        return;
    }

    try {
        const orderResponse = await fetch(`${API_BASE_URL}/ordini`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ tipo })
        });

        const ordine = await orderResponse.json();
        const promises = cart.map(item => fetch(`${API_BASE_URL}/ordini/${ordine.id}/items`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                id: item.id,
                nome: item.nome,
                price: item.prezzo,
                quantity: item.quantity,
                categoria: item.categoria
            })
        }));

        await Promise.all(promises);

        clearCart();
        showNotification(`✅ Ordine inviato! Numero ordine: #${ordine.id}`, 'success');
    } catch (error) {
        console.error('Errore invio ordine:', error);
        showNotification('❌ Errore durante l\'invio dell\'ordine', 'error');
    }
}

function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 22px;
        border-radius: 8px;
        color: white;
        background-color: ${type === 'success' ? '#27AE60' : type === 'warning' ? '#F39C12' : type === 'error' ? '#E74C3C' : '#3498DB'};
        box-shadow: 0 4px 12px rgba(0,0,0,0.25);
        z-index: 9999;
        animation: fadeIn 0.3s ease;
    `;
    notification.textContent = message;
    document.body.appendChild(notification);
    setTimeout(() => notification.remove(), 3000);
}

window.addEventListener('DOMContentLoaded', () => {
    loadMenuForUser();
    renderCart();
});
