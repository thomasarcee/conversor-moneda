package com.thomas.conversor.url;

public final class ExchangeRateApiUrlBuilder {

    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6";

    private ExchangeRateApiUrlBuilder() {
    }

    public static String buildPairUrl(String apiKey, String baseCurrency, String targetCurrency) {
        validateApiKey(apiKey);
        String normalizedBaseCurrency = normalizeCurrency(baseCurrency, "baseCurrency");
        String normalizedTargetCurrency = normalizeCurrency(targetCurrency, "targetCurrency");

        return String.format(
                "%s/%s/pair/%s/%s",
                BASE_URL,
                apiKey.trim(),
                normalizedBaseCurrency,
                normalizedTargetCurrency
        );
    }

    public static String buildLatestRatesUrl(String apiKey, String baseCurrency) {
        validateApiKey(apiKey);
        String normalizedBaseCurrency = normalizeCurrency(baseCurrency, "baseCurrency");

        return String.format(
                "%s/%s/latest/%s",
                BASE_URL,
                apiKey.trim(),
                normalizedBaseCurrency
        );
    }

    private static void validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("La API key no puede estar vacia.");
        }
    }

    private static String normalizeCurrency(String currency, String fieldName) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("La moneda " + fieldName + " no puede estar vacia.");
        }

        return currency.trim().toUpperCase();
    }
}