package com.thomas.conversor.rates;

import com.thomas.conversor.currency.SupportedCurrency;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExchangeRateTable {

    private final String result;
    private final SupportedCurrency baseCurrency;
    private final Map<SupportedCurrency, Double> conversionRates;

    public ExchangeRateTable(
            String result,
            SupportedCurrency baseCurrency,
            Map<SupportedCurrency, Double> conversionRates
    ) {
        if (result == null || result.isBlank()) {
            throw new IllegalArgumentException("El resultado de la respuesta no puede estar vacio.");
        }
        if (baseCurrency == null) {
            throw new IllegalArgumentException("La moneda base no puede ser nula.");
        }
        if (conversionRates == null || conversionRates.isEmpty()) {
            throw new IllegalArgumentException("La tabla de tasas no puede estar vacia.");
        }

        this.result = result;
        this.baseCurrency = baseCurrency;
        this.conversionRates = Collections.unmodifiableMap(new LinkedHashMap<>(conversionRates));
    }

    public String getResult() {
        return result;
    }

    public SupportedCurrency getBaseCurrency() {
        return baseCurrency;
    }

    public Map<SupportedCurrency, Double> getConversionRates() {
        return conversionRates;
    }

    public double getRateFor(SupportedCurrency targetCurrency) {
        if (targetCurrency == null) {
            throw new IllegalArgumentException("La moneda destino no puede ser nula.");
        }

        Double rate = conversionRates.get(targetCurrency);
        if (rate == null) {
            throw new IllegalArgumentException(
                    "No hay una tasa disponible para la moneda: " + targetCurrency.getCode()
            );
        }

        return rate;
    }

    @Override
    public String toString() {
        return "ExchangeRateTable{" +
                "result='" + result + '\'' +
                ", baseCurrency=" + baseCurrency.getCode() +
                ", conversionRates=" + conversionRates +
                '}';
    }
}