package com.sberStart.bank_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CurrencyNumberAccountBankServiceImpTest {
    private CurrencyConverterBankServerImp currencyConverter;

    @BeforeEach
    void init() {
        currencyConverter = new CurrencyConverterBankServerImp();
    }

    @ParameterizedTest
    @CsvSource({
            "USD, EUR, 100, 0.92",
            "RUB, USD, 50, 0.011",
            "RUB, RUB, 50, 1"
    })
    public void convertCurrencyTest(String fromCurrency, String toCurrency, BigDecimal amount, double expectedConversion) {
        double conversionRate = currencyConverter.convertCurrency(amount, fromCurrency, toCurrency);
        assertEquals(expectedConversion, conversionRate);
    }

    @Test
    public void convertCurrencyTestThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> currencyConverter.convertCurrency(BigDecimal.valueOf(1000), "RRR", "RUB"));
    }
}
