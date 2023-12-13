package com.sberStart.bank_api.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;

@Service
public class GenerateNumberAccountBankServiceImp implements GenerateNumberAccountBankService {
    private static final int ACCOUNT_NUMBER_LENGTH = 16;
    private final Random random = new Random();

    @Override
    public String generateNumberAccount() {
        return random.ints(ACCOUNT_NUMBER_LENGTH, 0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }
}
