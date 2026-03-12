package com.thomas.conversor;

import com.thomas.conversor.api.ExchangeRateApiClient;
import com.thomas.conversor.config.AppConfig;
import com.thomas.conversor.console.CurrencyConverterConsoleApp;
import com.thomas.conversor.history.ConversionHistoryService;
import com.thomas.conversor.service.CurrencyConversionService;
import com.thomas.conversor.service.SupportedCurrencyFilter;

public class Main {

    public static void main(String[] args) {
        try {
            AppConfig config = AppConfig.load();

            ExchangeRateApiClient apiClient = new ExchangeRateApiClient(config.getApiKey());
            CurrencyConversionService conversionService = new CurrencyConversionService();
            SupportedCurrencyFilter currencyFilter = new SupportedCurrencyFilter();
            ConversionHistoryService historyService = new ConversionHistoryService();

            CurrencyConverterConsoleApp consoleApp = new CurrencyConverterConsoleApp(
                    apiClient,
                    conversionService,
                    currencyFilter,
                    historyService
            );

            consoleApp.start();
        } catch (IllegalStateException exception) {
            System.err.println("Error de configuracion: " + exception.getMessage());
        } catch (Exception exception) {
            System.err.println("No fue posible iniciar la aplicacion: " + exception.getMessage());
        }
    }
}