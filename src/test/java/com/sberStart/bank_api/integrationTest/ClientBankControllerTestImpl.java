package com.sberStart.bank_api.integrationTest;

import com.sberStart.bank_api.dto.client.AccountResponseDTO;
import com.sberStart.bank_api.dto.client.BalanceResponseDTO;
import com.sberStart.bank_api.dto.client.OperationRequestDTO;
import com.sberStart.bank_api.dto.client.OperationResponseDTO;
import com.sberStart.bank_api.dto.client.TransferCurrencyRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ClientBankControllerTestImpl implements BankControllerTest {
    private final String URL_API = "/v1/bank/client";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("Список счетов")
    public void getAllAccountsTest() {
        UUID idClient = UUID.fromString("d733dc7e-30d6-41eb-8aca-0aa670e318d9");
        ResponseEntity<List<AccountResponseDTO>> response =
                testRestTemplate.exchange(
                        URL_API + "/accounts/" + idClient, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Пополнение счета")
    public void createBalanceAccountTest() {
        UUID idAccount = UUID.fromString("cff31aa3-f4b6-4bf8-b2fa-431402f8ebd8");
        OperationRequestDTO operationRequest = OperationRequestDTO.builder()
                .id(idAccount)
                .amount(new BigDecimal(10))
                .bankConfirmationStatus(true)
                .build();
        ResponseEntity<OperationResponseDTO> response =
                testRestTemplate.postForEntity(URL_API + "/depositMoneyToAccount", operationRequest, OperationResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Баланс счета")
    public void getBalanceAccountByIdTest() {
        UUID idAccount = UUID.fromString("cff31aa3-f4b6-4bf8-b2fa-431402f8ebd8");
        ResponseEntity<BalanceResponseDTO> response =
                testRestTemplate.getForEntity(URL_API + "/balance/" + idAccount, BalanceResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Перевод средств")
    public void transferMoneyCurrencyTest() {
        UUID fromAccountID = UUID.fromString("cff31aa3-f4b6-4bf8-b2fa-431402f8ebd8");
        UUID toAccountID = UUID.fromString("cff31aa3-f4b6-4bf8-b2fa-431402f8ebd8");
        TransferCurrencyRequestDTO transferCurrencyRequestDTO = TransferCurrencyRequestDTO.builder()
                .fromAmountID(fromAccountID)
                .toAmountID(toAccountID)
                .fromCurrency("RUB")
                .toCurrency("RUB")
                .amount(new BigDecimal(20))
                .build();
        ResponseEntity<String> response =
                testRestTemplate.postForEntity(URL_API + "/transferCurrency", transferCurrencyRequestDTO, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

}
