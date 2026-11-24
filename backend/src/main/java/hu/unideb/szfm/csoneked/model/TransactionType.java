package hu.unideb.szfm.csoneked.model;

/**
 * Hancoin (Hc) belső kredit tranzakció típusok:
 * - TOPUP: Egyenlegfeltöltés / kezdő jóváírás (+)
 * - PURCHASE: Jegyvásárlás levonás (-)
 * - REFUND: Jegy lemondás visszatérítés (+)
 */
public enum TransactionType {
    TOPUP,
    PURCHASE,
    REFUND
}
