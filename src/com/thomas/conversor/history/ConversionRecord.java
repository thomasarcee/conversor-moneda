package com.thomas.conversor.history;

import com.thomas.conversor.currency.SupportedCurrency;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConversionRecord {

    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final SupportedCurrency baseCurrency;
    private final SupportedCurrency targetCurrency;
    private final double amount;
    private final double rate;
    private final double convertedAmount;
    private final LocalDateTime conversionDateTime;

    public ConversionRecord(
            SupportedCurrency baseCurrency,
            SupportedCurrency targetCurrency,
            double amount,
            double rate,
            double convertedAmount,
            LocalDateTime conversionDateTime
    ) {
        if (baseCurrency == null) {
            throw new IllegalArgumentException("La moneda base no puede ser nula.");
        }
        if (targetCurrency == null) {
            throw new IllegalArgumentException("La moneda destino no puede ser nula.");
        }
        if (conversionDateTime == null) {
            throw new IllegalArgumentException("La fecha y hora de conversion no puede ser nula.");
        }

        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
        this.rate = rate;
        this.convertedAmount = convertedAmount;
        this.conversionDateTime = conversionDateTime;
    }

    public SupportedCurrency getBaseCurrency() {
        return baseCurrency;
    }

    public SupportedCurrency getTargetCurrency() {
        return targetCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public double getRate() {
        return rate;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }

    public LocalDateTime getConversionDateTime() {
        return conversionDateTime;
    }

    public String getFormattedTimestamp() {
        return conversionDateTime.format(DISPLAY_FORMATTER);
    }
}