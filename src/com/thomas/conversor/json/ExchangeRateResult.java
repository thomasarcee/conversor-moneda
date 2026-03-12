package com.thomas.conversor.json;

public class ExchangeRateResult {

    private final String result;
    private final String baseCode;
    private final String targetCode;
    private final double conversionRate;
    private final Double conversionResult;

    public ExchangeRateResult(
            String result,
            String baseCode,
            String targetCode,
            double conversionRate,
            Double conversionResult
    ) {
        this.result = result;
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.conversionRate = conversionRate;
        this.conversionResult = conversionResult;
    }

    public String getResult() {
        return result;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public Double getConversionResult() {
        return conversionResult;
    }

    public boolean hasConversionResult() {
        return conversionResult != null;
    }

    @Override
    public String toString() {
        return "ExchangeRateResult{" +
                "result='" + result + '\'' +
                ", baseCode='" + baseCode + '\'' +
                ", targetCode='" + targetCode + '\'' +
                ", conversionRate=" + conversionRate +
                ", conversionResult=" + conversionResult +
                '}';
    }
}