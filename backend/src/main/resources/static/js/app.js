/**
 * ==========================================================================
 * C.S.Ő EVENT - Közös UI & Navigáció Kezelő (app.js)
 * Csőneked Csapat · Debreceni Egyetem
 * ==========================================================================
 */

// Toast értesítés megjelenítő
function showToast(message, type = 'info') {
  let container = document.getElementById('toastContainer');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toastContainer';
    container.className = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  
  let icon = 'ℹ️';
  if (type === 'success') icon = '✅';
  if (type === 'danger') icon = '❌';
  if (type === 'warning') icon = '⚠️';

  toast.innerHTML = `
    <span style="font-size: 1.25rem;">${icon}</span>
    <div style="flex-grow: 1; font-size: 0.92rem; font-weight: 500;">${message}</div>
    <button style="background: none; border: none; font-size: 1.1rem; cursor: pointer; color: var(--cso-gray);" onclick="this.parentElement.remove()">×</button>
  `;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateX(100%)';
    toast.style.transition = 'all 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, 4000);
}

// Modál ablak megnyitása
function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
  }
}

// Modál ablak bezárása
function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.remove('active');
    document.body.style.overflow = '';
  }
}

// Profil lenyíló menü váltója
function toggleProfileDropdown(event) {
  if (event) event.stopPropagation();
  const dropdown = document.getElementById('profileDropdown');
  if (dropdown) {
    dropdown.classList.toggle('open');
  }
}

// Kattintás modál háttérre vagy külső területre (menü bezárása)
document.addEventListener('click', (e) => {
  if (e.target.classList.contains('modal-overlay')) {
    e.target.classList.remove('active');
    document.body.style.overflow = '';
  }

  // Profil lenyíló bezárása ha kívülre kattint
  const dropdown = document.getElementById('profileDropdown');
  if (dropdown && !dropdown.contains(e.target)) {
    dropdown.classList.remove('open');
  }
});

// Fejléc és navigáció inicializálása
async function setupNavigation() {
  const currentUser = Auth.getUser();
  const currentRole = currentUser.role || 'GUEST';

  const userGreeting = document.getElementById('userGreeting');
  if (userGreeting) {
    if (currentRole === 'GUEST') {
      userGreeting.innerHTML = `
        <a href="auth.html" class="btn btn-outline btn-sm">Bejelentkezés</a>
        <a href="auth.html?mode=register" class="btn btn-primary btn-sm">Regisztráció</a>
      `;
    } else {
      const roleText = currentRole === 'ADMIN' ? 'Admin' : 'Felhasználó';
      userGreeting.innerHTML = `
        <!-- Profil Lenyíló Menü (Hancoin Vásárlás, Saját foglalások, Kilépés) -->
        <div class="profile-dropdown-wrapper" id="profileDropdown">
          <button type="button" class="profile-trigger-btn" onclick="toggleProfileDropdown(event)" title="Profil menü megnyitása">
            <div class="user-avatar-pill">${currentUser.name ? currentUser.name.charAt(0).toUpperCase() : 'U'}</div>
            <span>${currentUser.name}</span>
            <span class="profile-trigger-caret">▼</span>
          </button>

          <div class="profile-dropdown-menu">
            <div class="profile-dropdown-header">
              <span class="profile-dropdown-name">${currentUser.name}</span>
              <span class="profile-dropdown-email">${currentUser.email || 'Bejelentkezve'}</span>
              <span class="badge ${currentRole === 'ADMIN' ? 'badge-danger' : 'badge-info'}" style="margin-top: 0.4rem; width: fit-content; font-size: 0.7rem;">${roleText}</span>
            </div>

            <div class="profile-dropdown-hancoin">
              <div>
                <span style="display: block; font-size: 0.72rem; color: var(--cso-gray); text-transform: uppercase; font-weight: 700; letter-spacing: 0.5px;">Egyenleg</span>
                <strong id="dropdownBalanceText" style="color: var(--cso-orange); font-size: 1.05rem;">${formatCurrency(currentUser.balance || 0)}</strong>
              </div>
              <a href="wallet.html" class="btn btn-primary btn-sm" style="padding: 0.25rem 0.6rem; font-size: 0.75rem; text-decoration: none;">
                + Feltöltés
              </a>
            </div>

            <a href="wallet.html" class="profile-menu-item">
              <span>🪙</span> Hancoin Vásárlás
            </a>
            <a href="bookings.html" class="profile-menu-item">
              <span>🎟️</span> Saját foglalásaim
            </a>
            ${currentRole === 'ADMIN' ? `
              <a href="admin.html" class="profile-menu-item" style="color: var(--cso-orange-hover);">
                <span>⚙️</span> Adminisztrációs pult
              </a>
            ` : ''}

            <div class="profile-menu-divider"></div>

            <button type="button" onclick="Auth.logout()" class="profile-menu-item" style="color: var(--cso-danger);">
              <span>🚪</span> Kilépés
            </button>
          </div>
        </div>
      `;
    }
  }

  // Admin linkek láthatósága
  const adminNavLinks = document.querySelectorAll('.nav-admin-only');
  adminNavLinks.forEach(el => {
    el.style.display = currentRole === 'ADMIN' ? 'inline-flex' : 'none';
  });

  // Bejelentkezett felhasználói linkek
  const userNavLinks = document.querySelectorAll('.nav-user-only');
  userNavLinks.forEach(el => {
    el.style.display = (currentRole === 'USER' || currentRole === 'ADMIN') ? 'inline-flex' : 'none';
  });

  const currentYearSpan = document.getElementById('currentYear');
  if (currentYearSpan) {
    currentYearSpan.textContent = new Date().getFullYear();
  }

  // Frissítjük a felhasználó egyenlegét az SQLite adatbázisból ha be van jelentkezve
  if (currentUser && currentUser.id) {
    Auth.refreshUser().catch(() => {});
  }
}

// Egyenleg változás figyelése
window.addEventListener('balance-changed', (e) => {
  const dropdownBal = document.getElementById('dropdownBalanceText');
  if (dropdownBal) {
    dropdownBal.textContent = formatCurrency(e.detail.balance);
  }
});

// Adatbázis visszaállítása
async function handleResetData() {
  if (confirm('Biztosan visszaállítod az SQLite adatbázis adatait a kezdeti mintaadatokra?')) {
    try {
      await window.Api.resetData();
      showToast('Az SQLite adatbázis adatai sikeresen visszaálltak a mintaadatokra!', 'success');
      setTimeout(() => {
        window.location.reload();
      }, 700);
    } catch (e) {
      showToast('Hiba az adatok visszaállításakor: ' + e.message, 'danger');
    }
  }
}

document.addEventListener('DOMContentLoaded', () => {
  setupNavigation();
});
