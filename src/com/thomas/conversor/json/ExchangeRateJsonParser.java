package com.thomas.conversor.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.thomas.conversor.api.ExchangeRateApiException;
import com.thomas.conversor.currency.SupportedCurrency;
import com.thomas.conversor.rates.ExchangeRateTable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExchangeRateJsonParser {

    public ExchangeRateResult parsePairResponse(String json) {
        validateJson(json);

        JsonObject rootObject = parseRootObject(json);
        validateApiResult(rootObject);

        String result = getRequiredString(rootObject, "result");
        String baseCode = getRequiredString(rootObject, "base_code");
        String targetCode = getRequiredString(rootObject, "target_code");
        double conversionRate = getRequiredDouble(rootObject, "conversion_rate");
        Double conversionResult = getOptionalDouble(rootObject, "conversion_result");

        return new ExchangeRateResult(result, baseCode, targetCode, conversionRate, conversionResult);
    }

    public ExchangeRateTable parseRatesResponse(String json) {
        validateJson(json);

        JsonObject rootObject = parseRootObject(json);
        validateApiResult(rootObject);

        String result = getRequiredString(rootObject, "result");
        String baseCode = getRequiredString(rootObject, "base_code");
        SupportedCurrency baseCurrency = parseSupportedCurrency(baseCode);

        JsonObject conversionRatesObject = getRequiredObject(rootObject, "conversion_rates");
        Map<SupportedCurrency, Double> conversionRates = extractSupportedRates(conversionRatesObject);

        return new ExchangeRateTable(result, baseCurrency, conversionRates);
    }

    private void validateJson(String json) {
        if (json == null || json.isBlank()) {
            throw new ExchangeRateApiException("La respuesta JSON de la API llego vacia.");
        }
    }

    private JsonObject parseRootObject(String json) {
        try {
            JsonElement rootElement = JsonParser.parseString(json);
            if (!rootElement.isJsonObject()) {
                throw new ExchangeRateApiException("La respuesta de la API no tiene un formato JSON valido.");
            }

            return rootElement.getAsJsonObject();
        } catch (JsonParseException exception) {
            throw new ExchangeRateApiException("No se pudo interpretar la respuesta JSON de la API.", exception);
        }
    }

    private void validateApiResult(JsonObject jsonObject) {
        String result = getRequiredString(jsonObject, "result");
        if ("success".equalsIgnoreCase(result)) {
            return;
        }

        String errorType = getOptionalString(jsonObject, "error-type");
        if (errorType == null || errorType.isBlank()) {
            throw new ExchangeRateApiException("La API devolvio un error no especificado.");
        }

        throw new ExchangeRateApiException("La API devolvio un error: " + errorType);
    }

    private SupportedCurrency parseSupportedCurrency(String code) {
        return SupportedCurrency.fromCode(code)
                .orElseThrow(() -> new ExchangeRateApiException(
                        "La moneda no esta soportada por el challenge: " + code
                ));
    }

    private Map<SupportedCurrency, Double> extractSupportedRates(JsonObject conversionRatesObject) {
        Map<SupportedCurrency, Double> conversionRates = new LinkedHashMap<>();

        for (SupportedCurrency currency : SupportedCurrency.values()) {
            String code = currency.getCode();
            if (conversionRatesObject.has(code) && !conversionRatesObject.get(code).isJsonNull()) {
                conversionRates.put(currency, conversionRatesObject.get(code).getAsDouble());
            }
        }

        if (conversionRates.isEmpty()) {
            throw new ExchangeRateApiException(
                    "La respuesta no contiene tasas para las monedas soportadas del challenge."
            );
        }

        return conversionRates;
    }

    private JsonObject getRequiredObject(JsonObject jsonObject, String propertyName) {
        if (!jsonObject.has(propertyName) || jsonObject.get(propertyName).isJsonNull()) {
            throw new ExchangeRateApiException("Falta la propiedad requerida: " + propertyName);
        }

        return jsonObject.getAsJsonObject(propertyName);
    }

    private String getRequiredString(JsonObject jsonObject, String propertyName) {
        if (!jsonObject.has(propertyName) || jsonObject.get(propertyName).isJsonNull()) {
            throw new ExchangeRateApiException("Falta la propiedad requerida: " + propertyName);
        }

        return jsonObject.get(propertyName).getAsString();
    }

    private double getRequiredDouble(JsonObject jsonObject, String propertyName) {
        if (!jsonObject.has(propertyName) || jsonObject.get(propertyName).isJsonNull()) {
            throw new ExchangeRateApiException("Falta la propiedad requerida: " + propertyName);
        }

        return jsonObject.get(propertyName).getAsDouble();
    }

    private String getOptionalString(JsonObject jsonObject, String propertyName) {
        if (!jsonObject.has(propertyName) || jsonObject.get(propertyName).isJsonNull()) {
            return null;
        }

        return jsonObject.get(propertyName).getAsString();
    }

    private Double getOptionalDouble(JsonObject jsonObject, String propertyName) {
        if (!jsonObject.has(propertyName) || jsonObject.get(propertyName).isJsonNull()) {
            return null;
        }

        return jsonObject.get(propertyName).getAsDouble();
    }
}