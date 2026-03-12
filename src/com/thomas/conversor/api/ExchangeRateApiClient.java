package com.thomas.conversor.api;

import com.thomas.conversor.json.ExchangeRateJsonParser;
import com.thomas.conversor.json.ExchangeRateResult;
import com.thomas.conversor.rates.ExchangeRateTable;
import com.thomas.conversor.url.ExchangeRateApiUrlBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ExchangeRateApiClient {

    private static final Duration CONNECTION_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);

    private final String apiKey;
    private final HttpClient httpClient;
    private final ExchangeRateJsonParser jsonParser;

    public ExchangeRateApiClient(String apiKey) {
        this(
                apiKey,
                HttpClient.newBuilder()
                        .connectTimeout(CONNECTION_TIMEOUT)
                        .build(),
                new ExchangeRateJsonParser()
        );
    }

    public ExchangeRateApiClient(String apiKey, HttpClient httpClient, ExchangeRateJsonParser jsonParser) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("La API key no puede estar vacia.");
        }
        if (httpClient == null) {
            throw new IllegalArgumentException("El cliente HTTP no puede ser nulo.");
        }
        if (jsonParser == null) {
            throw new IllegalArgumentException("El parser JSON no puede ser nulo.");
        }

        this.apiKey = apiKey.trim();
        this.httpClient = httpClient;
        this.jsonParser = jsonParser;
    }

    public ExchangeRateResult fetchPairConversion(String baseCurrency, String targetCurrency) {
        String endpoint = ExchangeRateApiUrlBuilder.buildPairUrl(apiKey, baseCurrency, targetCurrency);
        HttpResponse<String> response = send(buildRequest(endpoint));

        validateHttpStatus(response);
        return jsonParser.parsePairResponse(response.body());
    }

    public ExchangeRateTable fetchExchangeRateTable(String baseCurrency) {
        String endpoint = ExchangeRateApiUrlBuilder.buildLatestRatesUrl(apiKey, baseCurrency);
        HttpResponse<String> response = send(buildRequest(endpoint));

        validateHttpStatus(response);
        return jsonParser.parseRatesResponse(response.body());
    }

    private HttpRequest buildRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(REQUEST_TIMEOUT)
                .header("Accept", "application/json")
                .GET()
                .build();
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException exception) {
            throw new ExchangeRateApiException("No se pudo conectar con la API de tasas de cambio.", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ExchangeRateApiException("La solicitud HTTP fue interrumpida.", exception);
        }
    }

    private void validateHttpStatus(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode < 300) {
            return;
        }

        throw new ExchangeRateApiException(
                "La API respondio con un codigo HTTP inesperado: " + statusCode
        );
    }
}