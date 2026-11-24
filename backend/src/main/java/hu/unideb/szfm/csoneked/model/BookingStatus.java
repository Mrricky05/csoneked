package hu.unideb.szfm.csoneked.model;

/**
 * Jegyfoglalás állapota:
 * - CONFIRMED: Érvényes aktív belépő
 * - CANCELLED: Lemondott jegy (a Hancoin visszatérítve, férőhely felszabadítva)
 */
public enum BookingStatus {
    CONFIRMED,
    CANCELLED
}
