package com.thomas.conversor.service;

import com.thomas.conversor.currency.SupportedCurrency;

public class ConversionResult {

    private final SupportedCurrency baseCurrency;
    private final SupportedCurrency targetCurrency;
    private final double amount;
    private final double rate;
    private final double convertedAmount;

    public ConversionResult(
            SupportedCurrency baseCurrency,
            SupportedCurrency targetCurrency,
            double amount,
            double rate,
            double convertedAmount
    ) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
        this.rate = rate;
        this.convertedAmount = convertedAmount;
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

    @Override
    public String toString() {
        return "ConversionResult{" +
                "baseCurrency=" + baseCurrency.getCode() +
                ", targetCurrency=" + targetCurrency.getCode() +
                ", amount=" + amount +
                ", rate=" + rate +
                ", convertedAmount=" + convertedAmount +
                '}';
    }
}