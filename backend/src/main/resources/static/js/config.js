/**
 * ==========================================================================
 * C.S.Ő EVENT - Konfiguráció és Segédfüggvények (config.js)
 * Csőneked Csapat · Debreceni Egyetem
 * ==========================================================================
 */

const CONFIG = {
  APP_NAME: 'C.S.Ő EVENT',
  APP_TAGLINE: 'Rendezvény- és Jegyfoglalási Portál',
  TEAM_NAME: 'Csőneked',
  CURRENCY: 'Hc', // Fizetőeszköz: HANCOIN (Hc kredit)
  
  // Hozzáférési szintek és szerepkörök
  ROLES: {
    GUEST: {
      id: 'GUEST',
      level: 3,
      name: 'Vendég látogató',
      description: 'Böngészés és eseményrészletek megtekintése'
    },
    USER: {
      id: 'USER',
      level: 2,
      name: 'Regisztrált felhasználó',
      description: 'Jegyfoglalás, Hancoin egyenlegkezelés, lemondás'
    },
    ADMIN: {
      id: 'ADMIN',
      level: 1,
      name: 'Rendszeradminisztrátor',
      description: 'Rendezvények, férőhely-kapacitások és jegyek felügyelete'
    }
  },

  // Esemény kategóriák
  CATEGORIES: [
    { id: 'all', name: 'Összes kategória' },
    { id: 'koncert', name: 'Koncert & Zene' },
    { id: 'szinhaz', name: 'Színház & Kultúra' },
    { id: 'fesztival', name: 'Fesztivál' },
    { id: 'konferencia', name: 'Konferencia & Workshop' },
    { id: 'sport', name: 'Sport & Szabadidő' }
  ],

  // Későbbi Java Backend (Spring Boot) REST API integrációs cím
  API_BASE_URL: 'http://localhost:8080/api',
  USE_BACKEND_API: true
};

/**
 * Hancoin (Hc) összeg formázása (pl. 4 500 Hc)
 */
function formatCurrency(amount) {
  if (amount === undefined || amount === null) return '0 ' + CONFIG.CURRENCY;
  return new Intl.NumberFormat('hu-HU').format(amount) + ' ' + CONFIG.CURRENCY;
}

/**
 * Dátum formázása magyar szabvány szerint (pl. 2026. július 24.)
 */
function formatDate(dateString) {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('hu-HU', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
}

/**
 * Rövid dátum formázás (pl. 2026.07.24.)
 */
function formatShortDate(dateString) {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('hu-HU', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });
}

/**
 * Egyedi foglalási kód generálása (pl. CSO-2026-X8B2)
 */
function generateBookingCode() {
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
  let randomPart = '';
  for (let i = 0; i < 4; i++) {
    randomPart += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return `CSO-2026-${randomPart}`;
}
