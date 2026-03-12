package com.thomas.conversor.currency;

import java.util.Arrays;
import java.util.Optional;

public enum SupportedCurrency {

    ARS("ARS", "Argentine Peso"),
    BOB("BOB", "Bolivian Boliviano"),
    BRL("BRL", "Brazilian Real"),
    CLP("CLP", "Chilean Peso"),
    COP("COP", "Colombian Peso"),
    EUR("EUR", "Euro"),
    GBP("GBP", "British Pound Sterling"),
    MXN("MXN", "Mexican Peso"),
    PEN("PEN", "Peruvian Sol"),
    PYG("PYG", "Paraguayan Guarani"),
    USD("USD", "United States Dollar"),
    UYU("UYU", "Uruguayan Peso");

    private final String code;
    private final String displayName;

    SupportedCurrency(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static boolean isSupported(String code) {
        return fromCode(code).isPresent();
    }

    public static Optional<SupportedCurrency> fromCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }

        String normalizedCode = code.trim().toUpperCase();
        return Arrays.stream(values())
                .filter(currency -> currency.code.equals(normalizedCode))
                .findFirst();
    }
}