const API_BASE_URL = "http://localhost:8080/api";

const HARD_CODED_MENU = [
    { id: 1, nome: 'Caffe', prezzo: 1.50, categoria: 'Bevande' },
    { id: 2, nome: 'Cappuccino', prezzo: 2.00, categoria: 'Bevande' },
    { id: 3, nome: 'Espresso', prezzo: 1.00, categoria: 'Bevande' },
    { id: 4, nome: 'Latte', prezzo: 2.50, categoria: 'Bevande' },
    { id: 5, nome: 'Acqua', prezzo: 0.50, categoria: 'Bevande' },
    { id: 6, nome: 'Bibita', prezzo: 2.00, categoria: 'Bevande' },
    { id: 7, nome: 'Panino Prosciutto', prezzo: 4.00, categoria: 'Panini' },
    { id: 8, nome: 'Panino Formaggio', prezzo: 3.50, categoria: 'Panini' },
    { id: 9, nome: 'Panino Mortadella', prezzo: 3.80, categoria: 'Panini' },
    { id: 10, nome: 'Panino Pollo', prezzo: 4.50, categoria: 'Panini' },
    { id: 11, nome: 'Cornetto', prezzo: 1.20, categoria: 'Dolci' },
    { id: 12, nome: 'Brioche', prezzo: 1.50, categoria: 'Dolci' },
    { id: 13, nome: 'Torta', prezzo: 3.00, categoria: 'Dolci' },
    { id: 14, nome: 'Biscotti', prezzo: 1.00, categoria: 'Dolci' },
    { id: 15, nome: 'Pizza al taglio', prezzo: 2.50, categoria: 'Snack' },
    { id: 16, nome: 'Focaccia', prezzo: 2.00, categoria: 'Snack' },
    { id: 17, nome: 'Taralli', prezzo: 1.50, categoria: 'Snack' }
];

const HARD_CODED_INVENTORY = HARD_CODED_MENU.reduce((obj, item) => {
    obj[item.nome] = 100;
    return obj;
}, {});

let menuItems = [];
let cart = [];
let inventory = {};

function formatCurrency(amount) {
    return new Intl.NumberFormat('it-IT', {
        style: 'currency',
        currency: 'EUR'
    }).format(amount);
}

function getStoredInventory() {
    const stored = localStorage.getItem('barInventory');
    return stored ? JSON.parse(stored) : { ...HARD_CODED_INVENTORY };
}

function saveInventory(data) {
    localStorage.setItem('barInventory', JSON.stringify(data));
}

function initializeInventory() {
    if (!localStorage.getItem('barInventory')) {
        saveInventory(HARD_CODED_INVENTORY);
    }
    inventory = getStoredInventory();
}

function getInventoryQuantity(nome) {
    return inventory[nome] || 0;
}

function decrementInventory(cartItems) {
    cartItems.forEach(item => {
        const current = inventory[item.nome] || 0;
        inventory[item.nome] = Math.max(0, current - item.quantity);
    });
    saveInventory(inventory);
}

async function loadMenuForUser() {
    menuItems = HARD_CODED_MENU;
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
        const available = getInventoryQuantity(item.nome);
        const disabled = available <= 0 ? 'disabled' : '';
        const stockLabel = available <= 0 ? ' (Esaurito)' : ` (${available} disponibili)`;

        const card = document.createElement('div');
        card.className = 'menu-card';
        card.innerHTML = `
            <div class="category">${item.categoria}</div>
            <h4>${item.nome}${stockLabel}</h4>
            <div class="price">${formatCurrency(item.prezzo)}</div>
            <div class="menu-card-actions">
                <button class="btn-primary" onclick="addToCart(${item.id})" ${disabled}>Aggiungi</button>
            </div>
        `;
        menuGrid.appendChild(card);
    });
}

function addToCart(itemId) {
    const item = menuItems.find(p => p.id === itemId);
    if (!item) return;

    const available = getInventoryQuantity(item.nome);
    const currentInCart = cart.find(c => c.id === itemId)?.quantity || 0;
    if (currentInCart + 1 > available) {
        showNotification(`❌ Scorte insufficienti per ${item.nome}`, 'error');
        return;
    }

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
    const available = getInventoryQuantity(item.nome);
    if (change > 0 && item.quantity + 1 > available) {
        showNotification(`❌ Scorte insufficienti per ${item.nome}`, 'error');
        return;
    }
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

    // Controlla disponibilità prima di confermare
    for (const item of cart) {
        const available = getInventoryQuantity(item.nome);
        if (item.quantity > available) {
            showNotification(`❌ Scorte insufficienti per ${item.nome}`, 'error');
            return;
        }
    }

    decrementInventory(cart);
    saveInventory(inventory);
    initializeInventory();
    loadMenuForUser();
    clearCart();

    showNotification('✅ Ordine inviato! Scorte aggiornate.', 'success');
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
    initializeInventory();
    loadMenuForUser();
    renderCart();
});
