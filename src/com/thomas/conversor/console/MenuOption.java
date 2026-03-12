package com.thomas.conversor.console;

import com.thomas.conversor.currency.SupportedCurrency;

import java.util.Arrays;
import java.util.Optional;

public enum MenuOption {

    USD_TO_ARS(1, "Dolar =>> Peso argentino", SupportedCurrency.USD, SupportedCurrency.ARS),
    ARS_TO_USD(2, "Peso argentino =>> Dolar", SupportedCurrency.ARS, SupportedCurrency.USD),
    USD_TO_BRL(3, "Dolar =>> Real brasileño", SupportedCurrency.USD, SupportedCurrency.BRL),
    BRL_TO_USD(4, "Real brasileño =>> Dolar", SupportedCurrency.BRL, SupportedCurrency.USD),
    USD_TO_COP(5, "Dolar =>> Peso colombiano", SupportedCurrency.USD, SupportedCurrency.COP),
    COP_TO_USD(6, "Peso colombiano =>> Dolar", SupportedCurrency.COP, SupportedCurrency.USD),
    USD_TO_BOB(7, "Dolar =>> Boliviano boliviano", SupportedCurrency.USD, SupportedCurrency.BOB),
    BOB_TO_USD(8, "Boliviano boliviano =>> Dolar", SupportedCurrency.BOB, SupportedCurrency.USD),
    USD_TO_CLP(9, "Dolar =>> Peso chileno", SupportedCurrency.USD, SupportedCurrency.CLP),
    CLP_TO_USD(10, "Peso chileno =>> Dolar", SupportedCurrency.CLP, SupportedCurrency.USD),
    SHOW_HISTORY(11, "Ver historial", null, null),
    EXIT(12, "Salir", null, null);

    private final int optionNumber;
    private final String description;
    private final SupportedCurrency baseCurrency;
    private final SupportedCurrency targetCurrency;

    MenuOption(
            int optionNumber,
            String description,
            SupportedCurrency baseCurrency,
            SupportedCurrency targetCurrency
    ) {
        this.optionNumber = optionNumber;
        this.description = description;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
    }

    public int getOptionNumber() {
        return optionNumber;
    }

    public String getDescription() {
        return description;
    }

    public SupportedCurrency getBaseCurrency() {
        return baseCurrency;
    }

    public SupportedCurrency getTargetCurrency() {
        return targetCurrency;
    }

    public boolean isExit() {
        return this == EXIT;
    }

    public boolean isHistory() {
        return this == SHOW_HISTORY;
    }

    public static Optional<MenuOption> fromNumber(int optionNumber) {
        return Arrays.stream(values())
                .filter(option -> option.optionNumber == optionNumber)
                .findFirst();
    }
}