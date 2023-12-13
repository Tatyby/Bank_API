package com.sberStart.bank_api.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface CurrencyConverterBankServer {
    void addExchangeRate(String fromCurrency, String toCurrency, double rate);

    double convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency);
}
