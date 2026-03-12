package com.thomas.conversor.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

public final class LocalPropertiesConfig {

    private static final Path LOCAL_PROPERTIES_PATH = Path.of("local.properties");

    private LocalPropertiesConfig() {
    }

    public static Optional<String> read(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("La clave de configuracion no puede estar vacia.");
        }

        if (!Files.exists(LOCAL_PROPERTIES_PATH)) {
            return Optional.empty();
        }

        Properties properties = new Properties();

        try (InputStream inputStream = Files.newInputStream(LOCAL_PROPERTIES_PATH)) {
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "No se pudo leer el archivo local.properties.",
                    exception
            );
        }

        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(value.trim());
    }
}