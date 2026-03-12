package com.thomas.conversor.console;

import com.thomas.conversor.api.ExchangeRateApiClient;
import com.thomas.conversor.api.ExchangeRateApiException;
import com.thomas.conversor.currency.SupportedCurrency;
import com.thomas.conversor.history.ConversionHistoryService;
import com.thomas.conversor.history.ConversionRecord;
import com.thomas.conversor.rates.ExchangeRateTable;
import com.thomas.conversor.service.ConversionResult;
import com.thomas.conversor.service.CurrencyConversionService;
import com.thomas.conversor.service.SupportedCurrencyFilter;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CurrencyConverterConsoleApp {

    private final ExchangeRateApiClient apiClient;
    private final CurrencyConversionService conversionService;
    private final SupportedCurrencyFilter currencyFilter;
    private final ConversionHistoryService historyService;
    private final Scanner scanner;

    public CurrencyConverterConsoleApp(
            ExchangeRateApiClient apiClient,
            CurrencyConversionService conversionService,
            SupportedCurrencyFilter currencyFilter,
            ConversionHistoryService historyService
    ) {
        if (apiClient == null) {
            throw new IllegalArgumentException("El cliente de API no puede ser nulo.");
        }
        if (conversionService == null) {
            throw new IllegalArgumentException("El servicio de conversion no puede ser nulo.");
        }
        if (currencyFilter == null) {
            throw new IllegalArgumentException("El filtro de monedas no puede ser nulo.");
        }
        if (historyService == null) {
            throw new IllegalArgumentException("El servicio de historial no puede ser nulo.");
        }

        this.apiClient = apiClient;
        this.conversionService = conversionService;
        this.currencyFilter = currencyFilter;
        this.historyService = historyService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printWelcome();

        boolean running = true;
        while (running) {
            printMenu();
            MenuOption selectedOption = readMenuOption();

            if (selectedOption.isExit()) {
                System.out.println("Gracias por usar el conversor de moneda.");
                running = false;
                continue;
            }

            if (selectedOption.isHistory()) {
                printHistory();
                continue;
            }

            double amount = readAmount();
            processConversion(selectedOption, amount);
            System.out.println();
        }
    }

    private void printWelcome() {
        System.out.println("**************************************************");
        System.out.println("Bienvenido al Conversor de Moneda");
        System.out.println("Seleccione una opcion para realizar la conversion.");
        System.out.println("Tambien puede consultar el historial de conversiones.");
        System.out.println("**************************************************");
        System.out.println();
    }

    private void printMenu() {
        for (MenuOption option : MenuOption.values()) {
            System.out.println(option.getOptionNumber() + ") " + option.getDescription());
        }
        System.out.println();
    }

    private MenuOption readMenuOption() {
        while (true) {
            System.out.print("Ingrese una opcion: ");
            String input = scanner.nextLine().trim();

            try {
                int selectedNumber = Integer.parseInt(input);
                Optional<MenuOption> selectedOption = MenuOption.fromNumber(selectedNumber);
                if (selectedOption.isPresent()) {
                    return selectedOption.get();
                }

                System.out.println("Elija una opcion valida.");
            } catch (NumberFormatException exception) {
                System.out.println("Entrada invalida. Debe ingresar un numero de opcion.");
            }
        }
    }

    private double readAmount() {
        while (true) {
            System.out.print("Ingrese el monto que desea convertir: ");
            String input = scanner.nextLine().trim().replace(',', '.');

            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0) {
                    System.out.println("El monto debe ser mayor que cero.");
                    continue;
                }

                return amount;
            } catch (NumberFormatException exception) {
                System.out.println("Entrada invalida. Debe ingresar un numero valido.");
            }
        }
    }

    private void processConversion(MenuOption selectedOption, double amount) {
        try {
            ExchangeRateTable exchangeRateTable = apiClient.fetchExchangeRateTable(
                    selectedOption.getBaseCurrency().getCode()
            );
            ExchangeRateTable filteredRates = currencyFilter.filterSupportedRates(exchangeRateTable);

            ConversionResult conversionResult = conversionService.convert(
                    amount,
                    selectedOption.getBaseCurrency(),
                    selectedOption.getTargetCurrency(),
                    filteredRates
            );

            historyService.register(conversionResult);
            printConversionResult(conversionResult);
        } catch (ExchangeRateApiException exception) {
            System.out.println("No fue posible obtener la tasa de cambio: " + exception.getMessage());
        } catch (IllegalArgumentException exception) {
            System.out.println("No fue posible realizar la conversion: " + exception.getMessage());
        }
    }

    private void printConversionResult(ConversionResult conversionResult) {
        SupportedCurrency baseCurrency = conversionResult.getBaseCurrency();
        SupportedCurrency targetCurrency = conversionResult.getTargetCurrency();

        System.out.println();
        System.out.println("Resultado de la conversion:");
        System.out.println("Moneda origen: " + baseCurrency.getCode() + " - " + baseCurrency.getDisplayName());
        System.out.println("Moneda destino: " + targetCurrency.getCode() + " - " + targetCurrency.getDisplayName());
        System.out.println("Monto ingresado: " + formatDecimal(conversionResult.getAmount()));
        System.out.println("Tasa aplicada: " + formatRate(conversionResult.getRate()));
        System.out.println("Monto convertido: " + formatDecimal(conversionResult.getConvertedAmount()));
    }

    private void printHistory() {
        System.out.println();
        System.out.println("Historial de conversiones:");

        if (historyService.isEmpty()) {
            System.out.println("Todavia no se registraron conversiones.");
            System.out.println();
            return;
        }

        List<ConversionRecord> records = historyService.findAll();
        for (int index = 0; index < records.size(); index++) {
            ConversionRecord record = records.get(index);
            System.out.println(formatHistoryLine(index + 1, record));
        }
        System.out.println();
    }

    private String formatHistoryLine(int index, ConversionRecord record) {
        return index + ") " +
                record.getFormattedTimestamp() + " | " +
                record.getBaseCurrency().getCode() + " -> " + record.getTargetCurrency().getCode() +
                " | monto=" + formatDecimal(record.getAmount()) +
                " | tasa=" + formatRate(record.getRate()) +
                " | resultado=" + formatDecimal(record.getConvertedAmount());
    }

    private String formatDecimal(double value) {
        return String.format("%.2f", value);
    }

    /**
     * Formato para la tasa: si es muy pequena (< 0,01) se muestran mas decimales
     * para que no aparezca como "0,00" (ej.: 0,0007 para ARS -> USD).
     */
    private String formatRate(double rate) {
        if (rate >= 0.01) {
            return String.format("%.2f", rate);
        }
        if (rate > 0) {
            return String.format("%.6f", rate);
        }
        return String.format("%.2f", rate);
    }
}