package com.sberStart.bank_api.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenerateNumberAccountBankServiceImpTest {
    GenerateNumberAccountBankServiceImp generateNumber = new GenerateNumberAccountBankServiceImp();

    @Test
    public void generateNumberAccountTest() {
        String actualNumber = generateNumber.generateNumberAccount();
        assertTrue(actualNumber.matches("\\d{16}"));
        assertFalse(actualNumber.matches("\\d{17}"));
    }
}
