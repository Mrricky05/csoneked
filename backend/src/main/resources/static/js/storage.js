/**
 * ==========================================================================
 * C.S.Ő EVENT - Adatkezelő Réteg (storage.js)
 * Szimulált SQLite Adatbázis Repostory & HANCOIN Kredit Rendszer
 * Csőneked Csapat · Debreceni Egyetem
 * ==========================================================================
 */

const STORAGE_KEYS = {
  EVENTS: 'cso_events_v2',
  BOOKINGS: 'cso_bookings_v2',
  USERS: 'cso_users_v2',
  CURRENT_USER: 'cso_current_user_v2',
  TRANSACTIONS: 'cso_transactions_v2'
};

// ==========================================================================
// Kezdeti Mintaadatok (Jegyárak Hancoinban - Hc)
// ==========================================================================
const DEFAULT_EVENTS = [
  {
    id: 'evt-1',
    title: 'Campus Fesztivál 2026 - Nagyszínpad',
    category: 'fesztival',
    date: '2026-07-22',
    time: '18:00',
    location: 'Debrecen, Nagyerdő',
    price: 14900, // Hancoin (Hc)
    totalCapacity: 500,
    availableSeats: 85,
    organizer: 'Campus Kft.',
    description: 'Kelet-Magyarország legnagyobb könnyűzenei és kulturális fesztiválja a debreceni Nagyerdő szívében, nemzetközi és hazai fellépőkkel.',
    imageUrl: 'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=800&q=80',
    featured: true
  },
  {
    id: 'evt-2',
    title: 'Halott Pénz Évzáró Aréna Koncert',
    category: 'koncert',
    date: '2026-12-18',
    time: '20:00',
    location: 'Budapest, MVM Dome',
    price: 11900, // Hancoin (Hc)
    totalCapacity: 300,
    availableSeats: 32,
    organizer: 'Live Sound Prod',
    description: 'A közönségkedvenc hazai zenekar fergeteges évzáró koncertje egyedi látványvilággal, fénytechnikával és meglepetés vendégekkel.',
    imageUrl: 'https://images.unsplash.com/photo-1501386761578-eac5c94b800a?auto=format&fit=crop&w=800&q=80',
    featured: true
  },
  {
    id: 'evt-3',
    title: 'Csokonai Nemzeti Színház: A Pál utcai fiúk',
    category: 'szinhaz',
    date: '2026-11-20',
    time: '19:00',
    location: 'Debrecen, Csokonai Teátrum',
    price: 5900, // Hancoin (Hc)
    totalCapacity: 120,
    availableSeats: 14,
    organizer: 'Csokonai Színház',
    description: 'Molnár Ferenc világhírű regényének legendás zenés adaptációja Dés László és Geszti Péter dalaival, élő zenekari kísérettel.',
    imageUrl: 'https://images.unsplash.com/photo-1507676184212-d03ab07a01bf?auto=format&fit=crop&w=800&q=80',
    featured: true
  },
  {
    id: 'evt-4',
    title: 'Magyar Szoftvermérnöki Konferencia 2026',
    category: 'konferencia',
    date: '2026-11-28',
    time: '09:00',
    location: 'Debrecen, Kölcsey Központ',
    price: 19900, // Hancoin (Hc)
    totalCapacity: 250,
    availableSeats: 110,
    organizer: 'Informatikai Egyesület',
    description: 'Gyakorlati előadások Java, Cloud, AI és Microservice architektúrákról, kapcsolatépítés a legkorszerűbb hazai tech cégekkel.',
    imageUrl: 'https://images.unsplash.com/photo-1540575467063-178a50c2df87?auto=format&fit=crop&w=800&q=80',
    featured: false
  },
  {
    id: 'evt-5',
    title: 'Debrecen Bor- és Jazz Napok',
    category: 'fesztival',
    date: '2026-08-06',
    time: '16:00',
    location: 'Debrecen, Békás-tó partja',
    price: 4500, // Hancoin (Hc)
    totalCapacity: 400,
    availableSeats: 195,
    organizer: 'Főnix Rendezvényszervező',
    description: 'Kiváló hazai borászatok, kézműves gasztronómia és a legkiemelkedőbb jazz muzsikusok hangulatos találkozása a vízparton.',
    imageUrl: 'https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?auto=format&fit=crop&w=800&q=80',
    featured: false
  },
  {
    id: 'evt-6',
    title: 'Dumaszínház Gálaest: Stand-up Bajnokság',
    category: 'szinhaz',
    date: '2026-12-04',
    time: '18:30',
    location: 'Budapest, Dumaszínház',
    price: 6500, // Hancoin (Hc)
    totalCapacity: 80,
    availableSeats: 0,
    organizer: 'Dumaszínház Kft.',
    description: 'Két óra tömény nevetés a hazai humor élvonalával. Figyelem: erre az előadásra minden jegy elkelt!',
    imageUrl: 'https://images.unsplash.com/photo-1514306191717-452ec28c7814?auto=format&fit=crop&w=800&q=80',
    featured: false
  }
];

const DEFAULT_USERS = [
  {
    id: 'usr-1',
    name: 'Kiss János',
    email: 'janos@csoneked.hu',
    password: 'password123',
    role: 'USER',
    balance: 45000 // Hancoin egyenleg (Hc)
  },
  {
    id: 'usr-admin',
    name: 'C.S.Ő Főadmin',
    email: 'admin@csoneked.hu',
    password: 'admin',
    role: 'ADMIN',
    balance: 150000 // Hancoin egyenleg (Hc)
  }
];

const DEFAULT_BOOKINGS = [
  {
    id: 'bkg-1',
    bookingCode: 'CSO-2026-K7W9',
    eventId: 'evt-1',
    eventTitle: 'Campus Fesztivál 2026 - Nagyszínpad',
    eventDate: '2026-07-22',
    eventLocation: 'Debrecen, Nagyerdő',
    userId: 'usr-1',
    userName: 'Kiss János',
    userEmail: 'janos@csoneked.hu',
    ticketCount: 2,
    unitPrice: 14900,
    totalPrice: 29800,
    bookingDate: '2026-10-15T14:32:00.000Z',
    status: 'CONFIRMED'
  }
];

const DEFAULT_TRANSACTIONS = [
  {
    id: 'tx-1',
    userId: 'usr-1',
    type: 'TOPUP', // TOPUP | PURCHASE | REFUND
    amount: 50000,
    description: 'Hancoin egyenlegfeltöltés (Bankkártya)',
    date: '2026-10-15T10:00:00.000Z'
  },
  {
    id: 'tx-2',
    userId: 'usr-1',
    type: 'PURCHASE',
    amount: -29800,
    description: 'Jegyvásárlás: Campus Fesztivál 2026 (2 db)',
    date: '2026-10-15T14:32:00.000Z'
  }
];

// ==========================================================================
// Adattár Kezelő Osztály (Storage Repository)
// ==========================================================================
class StorageRepository {
  constructor() {
    this.init();
  }

  init() {
    if (!localStorage.getItem(STORAGE_KEYS.EVENTS)) {
      this.saveEvents(DEFAULT_EVENTS);
    }
    if (!localStorage.getItem(STORAGE_KEYS.USERS)) {
      this.saveUsers(DEFAULT_USERS);
    }
    if (!localStorage.getItem(STORAGE_KEYS.BOOKINGS)) {
      this.saveBookings(DEFAULT_BOOKINGS);
    }
    if (!localStorage.getItem(STORAGE_KEYS.TRANSACTIONS)) {
      this.saveTransactions(DEFAULT_TRANSACTIONS);
    }
    if (!localStorage.getItem(STORAGE_KEYS.CURRENT_USER)) {
      this.setCurrentUser({
        id: 'guest',
        name: 'Vendég látogató',
        email: '',
        role: 'GUEST',
        balance: 0
      });
    }
  }

  // --- Rendezvények CRUD műveletei ---

  getEvents() {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.EVENTS);
      return data ? JSON.parse(data) : DEFAULT_EVENTS;
    } catch (e) {
      console.error('Hiba a rendezvények betöltésekor:', e);
      return DEFAULT_EVENTS;
    }
  }

  getEventById(id) {
    const events = this.getEvents();
    return events.find(e => e.id === id) || null;
  }

  saveEvents(events) {
    localStorage.setItem(STORAGE_KEYS.EVENTS, JSON.stringify(events));
  }

  createEvent(eventData) {
    const events = this.getEvents();
    const newEvent = {
      ...eventData,
      id: 'evt-' + Date.now(),
      availableSeats: Number(eventData.totalCapacity)
    };
    events.unshift(newEvent);
    this.saveEvents(events);
    return newEvent;
  }

  updateEvent(id, updatedFields) {
    const events = this.getEvents();
    const index = events.findIndex(e => e.id === id);
    if (index === -1) return null;

    const oldEvent = events[index];
    let newAvailableSeats = oldEvent.availableSeats;
    if (updatedFields.totalCapacity !== undefined && Number(updatedFields.totalCapacity) !== oldEvent.totalCapacity) {
      const diff = Number(updatedFields.totalCapacity) - oldEvent.totalCapacity;
      newAvailableSeats = Math.max(0, oldEvent.availableSeats + diff);
    }

    events[index] = {
      ...oldEvent,
      ...updatedFields,
      availableSeats: newAvailableSeats
    };
    this.saveEvents(events);
    return events[index];
  }

  deleteEvent(id) {
    let events = this.getEvents();
    const initialLen = events.length;
    events = events.filter(e => e.id !== id);
    this.saveEvents(events);
    return events.length < initialLen;
  }

  // --- Jegyfoglalások & Hancoin Levonás ---

  getBookings() {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.BOOKINGS);
      return data ? JSON.parse(data) : DEFAULT_BOOKINGS;
    } catch (e) {
      return DEFAULT_BOOKINGS;
    }
  }

  saveBookings(bookings) {
    localStorage.setItem(STORAGE_KEYS.BOOKINGS, JSON.stringify(bookings));
  }

  getUserBookings(userId) {
    const bookings = this.getBookings();
    return bookings.filter(b => b.userId === userId);
  }

  /**
   * Új jegyfoglalás rögzítése:
   * 1. Férőhely kapacitás ellenőrzése
   * 2. Hancoin (Hc) egyenleg ellenőrzése és levonása
   * 3. Tranzakció mentése
   */
  createBooking(bookingData) {
    const event = this.getEventById(bookingData.eventId);
    if (!event) {
      throw new Error('A keresett rendezvény nem található.');
    }

    const requestedTickets = Number(bookingData.ticketCount);
    if (requestedTickets <= 0) {
      throw new Error('A jegyek száma legalább 1 kell legyen.');
    }

    if (event.availableSeats < requestedTickets) {
      throw new Error(`Sajnáljuk, nincs elegendő szabad férőhely!`);
    }

    const totalPrice = requestedTickets * event.price;
    const currentUser = this.getCurrentUser();

    // Hancoin fedezet ellenőrzése
    if ((currentUser.balance || 0) < totalPrice) {
      throw new Error(`Nincs elegendő Hancoin (Hc) az egyenlegeden!\nSzükséges: ${formatCurrency(totalPrice)}, Jelenlegi egyenleg: ${formatCurrency(currentUser.balance || 0)}.\nKérjük, töltsd fel az egyenleged a Hancoin Vásárlás menüpontban!`);
    }

    // 1. Hancoin levonása a felhasználó egyenlegéből
    this.updateUserBalance(currentUser.id, -totalPrice, `Jegyvásárlás: ${event.title} (${requestedTickets} db)`);

    // 2. Férőhely levonása a rendezvényből
    event.availableSeats -= requestedTickets;
    this.updateEvent(event.id, { availableSeats: event.availableSeats });

    // 3. Foglalás rögzítése
    const bookings = this.getBookings();
    const newBooking = {
      id: 'bkg-' + Date.now(),
      bookingCode: generateBookingCode(),
      eventId: event.id,
      eventTitle: event.title,
      eventDate: event.date,
      eventLocation: event.location,
      userId: currentUser.id,
      userName: bookingData.userName,
      userEmail: bookingData.userEmail,
      ticketCount: requestedTickets,
      unitPrice: event.price,
      totalPrice: totalPrice,
      bookingDate: new Date().toISOString(),
      status: 'CONFIRMED'
    };

    bookings.unshift(newBooking);
    this.saveBookings(bookings);
    return newBooking;
  }

  /**
   * Jegyfoglalás lemondása:
   * 1. Férőhely visszatöltése a rendezvényhez
   * 2. Hancoin (Hc) visszatérítése a felhasználó egyenlegére!
   */
  cancelBooking(bookingId) {
    const bookings = this.getBookings();
    const booking = bookings.find(b => b.id === bookingId);
    if (!booking) {
      throw new Error('A foglalás nem található.');
    }

    if (booking.status === 'CANCELLED') {
      throw new Error('Ez a foglalás már korábban le lett mondva.');
    }

    booking.status = 'CANCELLED';
    this.saveBookings(bookings);

    // 1. Kapacitás visszatöltése
    const event = this.getEventById(booking.eventId);
    if (event) {
      const restoredSeats = Math.min(event.totalCapacity, event.availableSeats + booking.ticketCount);
      this.updateEvent(event.id, { availableSeats: restoredSeats });
    }

    // 2. Hancoin visszatérítés
    this.updateUserBalance(booking.userId, booking.totalPrice, `Jegy lemondás visszatérítés: ${booking.eventTitle}`);

    return booking;
  }

  // --- HANCOIN (Hc) Egyenleg és Tranzakciók ---

  getTransactions(userId = null) {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.TRANSACTIONS);
      const list = data ? JSON.parse(data) : DEFAULT_TRANSACTIONS;
      if (userId) {
        return list.filter(t => t.userId === userId);
      }
      return list;
    } catch (e) {
      return DEFAULT_TRANSACTIONS;
    }
  }

  saveTransactions(transactions) {
    localStorage.setItem(STORAGE_KEYS.TRANSACTIONS, JSON.stringify(transactions));
  }

  updateUserBalance(userId, deltaAmount, description) {
    const users = this.getUsers();
    const user = users.find(u => u.id === userId);
    if (!user) return;

    user.balance = Math.max(0, (user.balance || 0) + deltaAmount);
    this.saveUsers(users);

    // Ha az aktuális felhasználó az
    const cur = this.getCurrentUser();
    if (cur.id === userId) {
      cur.balance = user.balance;
      this.setCurrentUser(cur);
    }

    // Tranzakció naplózása
    const transactions = this.getTransactions();
    transactions.unshift({
      id: 'tx-' + Date.now(),
      userId: userId,
      type: deltaAmount >= 0 ? (description.includes('visszatérítés') ? 'REFUND' : 'TOPUP') : 'PURCHASE',
      amount: deltaAmount,
      description: description,
      date: new Date().toISOString()
    });
    this.saveTransactions(transactions);

    window.dispatchEvent(new CustomEvent('balance-changed', { detail: { balance: user.balance } }));
    return user.balance;
  }

  topupHancoin(amount, packageName = 'Egyenlegfeltöltés') {
    const cur = this.getCurrentUser();
    if (cur.role === 'GUEST') {
      throw new Error('A Hancoin feltöltéséhez kérjük előbb jelentkezz be!');
    }
    const newBal = this.updateUserBalance(cur.id, amount, `${packageName} (${formatCurrency(amount)})`);
    return newBal;
  }

  // --- Felhasználói munkamenet ---

  getUsers() {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.USERS);
      return data ? JSON.parse(data) : DEFAULT_USERS;
    } catch (e) {
      return DEFAULT_USERS;
    }
  }

  saveUsers(users) {
    localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify(users));
  }

  getCurrentUser() {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.CURRENT_USER);
      return data ? JSON.parse(data) : { id: 'guest', name: 'Vendég látogató', email: '', role: 'GUEST', balance: 0 };
    } catch (e) {
      return { id: 'guest', name: 'Vendég látogató', email: '', role: 'GUEST', balance: 0 };
    }
  }

  setCurrentUser(user) {
    localStorage.setItem(STORAGE_KEYS.CURRENT_USER, JSON.stringify(user));
    window.dispatchEvent(new CustomEvent('auth-changed', { detail: user }));
  }

  registerUser(userData) {
    const users = this.getUsers();
    const exists = users.find(u => u.email.toLowerCase() === userData.email.toLowerCase());
    if (exists) {
      throw new Error('Ezzel az e-mail címmel már regisztráltak!');
    }

    const newUser = {
      id: 'usr-' + Date.now(),
      name: userData.name,
      email: userData.email,
      password: userData.password,
      role: 'USER',
      balance: 15000 // Kezdő ajándék Hancoin kredit regisztrációkor!
    };

    users.push(newUser);
    this.saveUsers(users);

    // Kezdő bónusz tranzakció naplózása
    const transactions = this.getTransactions();
    transactions.unshift({
      id: 'tx-' + Date.now(),
      userId: newUser.id,
      type: 'TOPUP',
      amount: 15000,
      description: 'Regisztrációs kezdő Hancoin bónusz jóváírása',
      date: new Date().toISOString()
    });
    this.saveTransactions(transactions);

    this.setCurrentUser(newUser);
    return newUser;
  }

  loginUser(email, password) {
    const users = this.getUsers();
    const user = users.find(u => u.email.toLowerCase() === email.toLowerCase() && u.password === password);
    if (!user) {
      throw new Error('Hibás e-mail cím vagy jelszó!');
    }
    this.setCurrentUser(user);
    return user;
  }

  logout() {
    const guestUser = {
      id: 'guest',
      name: 'Vendég látogató',
      email: '',
      role: 'GUEST',
      balance: 0
    };
    this.setCurrentUser(guestUser);
    return guestUser;
  }

  resetToDefaults() {
    this.saveEvents(DEFAULT_EVENTS);
    this.saveUsers(DEFAULT_USERS);
    this.saveBookings(DEFAULT_BOOKINGS);
    this.saveTransactions(DEFAULT_TRANSACTIONS);
    this.logout();
    return true;
  }
}

window.DB = new StorageRepository();
