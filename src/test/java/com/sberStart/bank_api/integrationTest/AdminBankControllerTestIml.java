package com.sberStart.bank_api.integrationTest;

import com.sberStart.bank_api.dto.admin.ClientAdminResponseDTO;
import com.sberStart.bank_api.dto.admin.DataClientResponseDTO;
import com.sberStart.bank_api.dto.admin.NewClientAdminRequestDTO;
import com.sberStart.bank_api.dto.admin.OpenAccountRequestDTO;
import com.sberStart.bank_api.dto.admin.OpenAccountResponseDTO;
import com.sberStart.bank_api.entity.AccountBankEntity;
import com.sberStart.bank_api.entity.ClientBankEntity;
import com.sberStart.bank_api.repository.AccountBankRepository;
import com.sberStart.bank_api.repository.ClientBankRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AdminBankControllerTestIml implements BankControllerTest {
    private final String URL_API = "/v1/bank/admin";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ClientBankRepository clientBankRepository;
    @Autowired
    private AccountBankRepository accountBankRepository;

    @Test
    @DisplayName("Создание нового клиента банка")
    public void createNewClientTest() {
        NewClientAdminRequestDTO newClient = NewClientAdminRequestDTO.builder()
                .firstName("Test")
                .lastName("Test")
                .phoneNumber("+7-9672115269")
                .email("test@mail.ru")
                .build();
        ResponseEntity<ClientAdminResponseDTO> response =
                testRestTemplate.postForEntity(URL_API + "/newClient", newClient, ClientAdminResponseDTO.class);
        Optional<ClientBankEntity> clientBankActual = clientBankRepository.findById(response.getBody().getId());
        ClientBankEntity clientExpected = ClientBankEntity.builder()
                .id(clientBankActual.get().getId())
                .firstName("Test")
                .lastName("Test")
                .phoneNumber("+7-9672115269")
                .email("test@mail.ru")
                .createdTime(clientBankActual.get().getCreatedTime())
                .account(new ArrayList<>())
                .build();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertEquals(clientExpected, clientBankActual.get());
    }

    @Test
    @DisplayName("Информация по клиенту банка")
    public void getDataClientTest() {
        UUID idClient = UUID.fromString("d733dc7e-30d6-41eb-8aca-0aa670e318d9");
        ResponseEntity<DataClientResponseDTO> response =
                testRestTemplate.getForEntity(URL_API + "/dataClient/" + idClient, DataClientResponseDTO.class);
        Optional<ClientBankEntity> clientBankActual = clientBankRepository.findById(response.getBody().getId());
        clientBankActual.get().setAccount(new ArrayList<>());
        ClientBankEntity clientBankExpected = ClientBankEntity.builder()
                .id(clientBankActual.get().getId())
                .firstName("Татьяна")
                .lastName("Буянова")
                .phoneNumber("+7-9672115269")
                .email("in_sdo@mail.ru")
                .account(new ArrayList<>())
                .createdTime(clientBankActual.get().getCreatedTime())
                .build();
        assertEquals(clientBankExpected, clientBankActual.get());
    }

    @Test
    @DisplayName("Открытие нового счета")
    public void openAccountByIdClient() {
        UUID idClient = UUID.fromString("d733dc7e-30d6-41eb-8aca-0aa670e318d9");
        OpenAccountRequestDTO newAccount = OpenAccountRequestDTO.builder()
                .id(idClient)
                .status(true)
                .currency("RUB")
                .build();
        ResponseEntity<OpenAccountResponseDTO> response =
                testRestTemplate.postForEntity(URL_API + "/openAccount", newAccount, OpenAccountResponseDTO.class);
        System.out.println(response.getBody());
        Optional<AccountBankEntity> accountBankActual = accountBankRepository.findById(response.getBody().getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        AccountBankEntity accountExpected = AccountBankEntity.builder()
                .id(accountBankActual.get().getId())
                .numberAccount(accountBankActual.get().getNumberAccount())
                .status(true)
                .currency("RUB")
                .balance(new BigDecimal(0))
                .operation(new ArrayList<>())
                .build();
        assertEquals(accountExpected.toString(), accountBankActual.get().toString());
    }

}