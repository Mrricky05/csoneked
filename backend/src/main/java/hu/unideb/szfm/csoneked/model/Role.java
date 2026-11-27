package hu.unideb.szfm.csoneked.model;

/**
 * Felhasználói jogosultsági szintek a bemutató (Slide 4) alapján:
 * - GUEST: Látogató (böngészés, megtekintés)
 * - USER: Regisztrált felhasználó (foglalás, Hancoin vásárlás, lemondás)
 * - ADMIN: Adminisztrátor (rendezvények CRUD, pontos férőhely kapacitások, felügyelet)
 */
public enum Role {
    GUEST,
    USER,
    ADMIN
}
