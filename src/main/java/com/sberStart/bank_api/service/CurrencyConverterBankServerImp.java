package com.sberStart.bank_api.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyConverterBankServerImp implements CurrencyConverterBankServer {
    private static final Map<String, Map<String, Double>> exchangeRates = new HashMap<>();

    public CurrencyConverterBankServerImp() {
        addExchangeRate("USD", "EUR", 0.92);
        addExchangeRate("RUB", "EUR", 0.01);
        addExchangeRate("RUB", "USD", 0.011);
        addExchangeRate("EUR", "USD", 1.09);
        addExchangeRate("EUR", "RUB", 97.13);
        addExchangeRate("USD", "RUB", 89.46);
        addExchangeRate("RUB", "RUB", 1);
        addExchangeRate("USD", "USD", 1);
        addExchangeRate("EUR", "EUR", 1);
    }

    /**
     * добавление курса валюты
     *
     * @param fromCurrency
     * @param toCurrency
     * @param rate
     */
    @Override
    public void addExchangeRate(String fromCurrency, String toCurrency, double rate) {
        exchangeRates.computeIfAbsent(fromCurrency, k -> new HashMap<>())
                .put(toCurrency, rate);

    }

    /**
     * конвертация валюты
     *
     * @param amount
     * @param fromCurrency
     * @param toCurrency
     * @return
     */
    @Override
    public double convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return 1;
        }
        Map<String, Double> rates = exchangeRates.get(fromCurrency);
        if (rates != null && rates.containsKey(toCurrency)) {
            return rates.get(toCurrency);
        } else {
            throw new IllegalArgumentException("Курс валют не найден");
        }
    }
}
