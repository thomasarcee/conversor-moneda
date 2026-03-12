package com.thomas.conversor.service;

import com.thomas.conversor.currency.SupportedCurrency;
import com.thomas.conversor.rates.ExchangeRateTable;

public class CurrencyConversionService {

    public ConversionResult convert(
            SupportedCurrency baseCurrency,
            SupportedCurrency targetCurrency,
            double amount,
            double conversionRate
    ) {
        validateCurrencies(baseCurrency, targetCurrency);
        validateAmount(amount);
        validateRate(conversionRate);

        double convertedAmount = amount * conversionRate;
        return new ConversionResult(baseCurrency, targetCurrency, amount, conversionRate, convertedAmount);
    }

    public ConversionResult convert(
            double amount,
            SupportedCurrency baseCurrency,
            SupportedCurrency targetCurrency,
            ExchangeRateTable exchangeRateTable
    ) {
        if (exchangeRateTable == null) {
            throw new IllegalArgumentException("La tabla de tasas no puede ser nula.");
        }
        if (!exchangeRateTable.getBaseCurrency().equals(baseCurrency)) {
            throw new IllegalArgumentException(
                    "La moneda base no coincide con la tabla de tasas recibida."
            );
        }

        double conversionRate = exchangeRateTable.getRateFor(targetCurrency);
        return convert(baseCurrency, targetCurrency, amount, conversionRate);
    }

    private void validateCurrencies(SupportedCurrency baseCurrency, SupportedCurrency targetCurrency) {
        if (baseCurrency == null) {
            throw new IllegalArgumentException("La moneda base no puede ser nula.");
        }
        if (targetCurrency == null) {
            throw new IllegalArgumentException("La moneda destino no puede ser nula.");
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a convertir debe ser mayor que cero.");
        }
    }

    private void validateRate(double conversionRate) {
        if (conversionRate <= 0) {
            throw new IllegalArgumentException("La tasa de conversion debe ser mayor que cero.");
        }
    }
}