package com.thomas.conversor.config;

import java.util.Optional;

public class AppConfig {

    private static final String ENV_API_KEY = "EXCHANGE_API_KEY";
    private static final String FILE_API_KEY = "exchange.api.key";

    private final String apiKey;

    private AppConfig(String apiKey) {
        this.apiKey = apiKey;
    }

    public static AppConfig load() {
        return new AppConfig(resolveApiKey());
    }

    public String getApiKey() {
        return apiKey;
    }

    private static String resolveApiKey() {
        Optional<String> apiKeyFromEnv = readEnvironmentVariable(ENV_API_KEY);
        if (apiKeyFromEnv.isPresent()) {
            return apiKeyFromEnv.get();
        }

        Optional<String> apiKeyFromFile = LocalPropertiesConfig.read(FILE_API_KEY);
        if (apiKeyFromFile.isPresent()) {
            return apiKeyFromFile.get();
        }

        throw new IllegalStateException(
                "No se encontro la API key. Configura la variable de entorno EXCHANGE_API_KEY " +
                "o crea un archivo local.properties con exchange.api.key=TU_API_KEY."
        );
    }

    private static Optional<String> readEnvironmentVariable(String variableName) {
        String value = System.getenv(variableName);
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(value.trim());
    }
}