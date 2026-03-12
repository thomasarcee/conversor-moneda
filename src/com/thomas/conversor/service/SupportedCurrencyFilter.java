package com.thomas.conversor.service;

import com.thomas.conversor.currency.SupportedCurrency;
import com.thomas.conversor.rates.ExchangeRateTable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SupportedCurrencyFilter {

    public boolean isSupported(String currencyCode) {
        return SupportedCurrency.isSupported(currencyCode);
    }

    public ExchangeRateTable filterSupportedRates(ExchangeRateTable exchangeRateTable) {
        if (exchangeRateTable == null) {
            throw new IllegalArgumentException("La tabla de tasas no puede ser nula.");
        }

        Map<SupportedCurrency, Double> filteredRates = new LinkedHashMap<>();
        for (SupportedCurrency currency : SupportedCurrency.values()) {
            Double rate = exchangeRateTable.getConversionRates().get(currency);
            if (rate != null) {
                filteredRates.put(currency, rate);
            }
        }

        if (filteredRates.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron monedas soportadas en la tabla de tasas.");
        }

        return new ExchangeRateTable(
                exchangeRateTable.getResult(),
                exchangeRateTable.getBaseCurrency(),
                filteredRates
        );
    }
}