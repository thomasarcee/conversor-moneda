# Conversor de Moneda

Proyecto Java de consola que consume la API [ExchangeRate-API](https://www.exchangerate-api.com/) para convertir entre las monedas del challenge.

## Requisitos

- **Java 21**
- API key gratuita de [exchangerate-api.com](https://www.exchangerate-api.com/)

## Configuración

1. Crea un archivo `local.properties` en la raíz del proyecto con:
   ```properties
   exchange.api.key=TU_API_KEY
   ```
2. O define la variable de entorno `EXCHANGE_API_KEY` con tu clave.

## Ejecución

- **IntelliJ IDEA:** Abre el proyecto, configura el SDK a Java 21 y ejecuta la clase `com.thomas.conversor.Main`.
- **Línea de comandos:** Compila desde la raíz con el classpath que incluya `src` y la librería Gson, luego ejecuta `com.thomas.conversor.Main`.

## Monedas soportadas

USD, ARS, BOB, BRL, CLP, COP (según el challenge).

## Tecnologías

- Java 21
- `java.net.http` (HttpClient)
- Gson para JSON
- Sin Maven/Gradle (dependencias manuales en IntelliJ).
