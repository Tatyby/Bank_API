package com.sberStart.bank_api.service.client;

import com.sberStart.bank_api.dto.client.AccountResponseDTO;
import com.sberStart.bank_api.dto.client.BalanceResponseDTO;
import com.sberStart.bank_api.dto.client.OperationRequestDTO;
import com.sberStart.bank_api.dto.client.OperationResponseDTO;
import com.sberStart.bank_api.dto.client.TransferCurrencyRequestDTO;
import com.sberStart.bank_api.entity.AccountBankEntity;
import com.sberStart.bank_api.entity.ClientBankEntity;
import com.sberStart.bank_api.entity.OperationBankEntity;
import com.sberStart.bank_api.exception.BankException;
import com.sberStart.bank_api.repository.AccountBankRepository;
import com.sberStart.bank_api.repository.ClientBankRepository;
import com.sberStart.bank_api.repository.OperationBankRepository;
import com.sberStart.bank_api.service.CurrencyConverterBankServerImp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientBankServiceImpTest {
    @Mock
    OperationBankRepository operationBankRepository;
    @Mock
    private AccountBankRepository accountBankRepository;
    @Mock
    private ClientBankRepository clientBankRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private CurrencyConverterBankServerImp currencyConverterBankServer;
    @InjectMocks
    private ClientBankServiceImp clientBankService;

    @AfterEach
    public void reset() {
       Mockito.reset(currencyConverterBankServer, mapper, clientBankRepository, accountBankRepository, operationBankRepository);
    }

    @Test
    @DisplayName("получение всех счетов у клиента по его id")
    public void getAllAccountsTest() {
        UUID clientId = UUID.randomUUID();
        List<AccountBankEntity> accountBankEntitiesExpected = Arrays.asList(
                AccountBankEntity.builder()
                        .id(UUID.randomUUID())
                        .numberAccount("12345678910111213148")
                        .build(),
                AccountBankEntity.builder()
                        .id(UUID.randomUUID())
                        .numberAccount("12345678910111213149")
                        .build());
        var clientBank = ClientBankEntity.builder()
                .id(clientId)
                .account(accountBankEntitiesExpected)
                .build();
        List<AccountResponseDTO> expectedList = Arrays.asList(
                new AccountResponseDTO(clientId, "12345678910111213149"),
                new AccountResponseDTO(clientId, "12345678910111213148")
        );
        when(clientBankRepository.findById(clientId))
                .thenReturn(Optional.of(clientBank));
        when(mapper.map(any(), eq(AccountResponseDTO.class)))
                .thenReturn(new AccountResponseDTO(clientId, "12345678910111213149"),
                        new AccountResponseDTO(clientId, "12345678910111213148"));
        List<AccountResponseDTO> actualList = clientBankService.getAllAccounts(clientId);
        assertEquals(expectedList, actualList);
        verify(clientBankRepository, times(1)).findById(clientId);
        verify(mapper, times(accountBankEntitiesExpected.size())).map(any(), eq(AccountResponseDTO.class));
    }

    @Test
    @DisplayName("получение всех счетов у клиента по его id, если id не нашелся в БД")
    public void getAllAccountsTestThrows() {
        UUID clientId = UUID.randomUUID();
        when(clientBankRepository.findById(clientId)).thenReturn(Optional.empty());
        assertThrows(BankException.class, () -> clientBankService.getAllAccounts(clientId));
        verify(clientBankRepository, times(1)).findById(clientId);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("пополнение счета по его id")
    public void createBalanceAccountTest() {
        UUID accountId = UUID.randomUUID();
        UUID operationId = UUID.randomUUID();
        var operationDTO = OperationRequestDTO.builder()
                .id(accountId)
                .amount(new BigDecimal(1000))
                .bankConfirmationStatus(true)
                .build();
        var accountBank = AccountBankEntity.builder()
                .id(accountId)
                .balance(new BigDecimal(5000))
                .status(true)
                .build();
        var operationResponseDTOExpected = OperationResponseDTO.builder()
                .id(operationId)
                .amount(new BigDecimal(2000))
                .build();
        when(accountBankRepository.findById(accountId))
                .thenReturn(Optional.of(accountBank));
        when(mapper.map(any(), eq(OperationResponseDTO.class))).thenReturn(operationResponseDTOExpected);
        var operationResponseDTOActual = clientBankService.createBalanceAccount(operationDTO);
        assertEquals(operationResponseDTOExpected, operationResponseDTOActual);
    }

    @Test
    @DisplayName("пополнение счета при не найденом id счета в БД")
    public void createBalanceAccountTestThrows() {
        UUID accountId = UUID.randomUUID();
        var operationDTO = OperationRequestDTO.builder()
                .id(accountId)
                .amount(new BigDecimal(1000))
                .build();
        when(accountBankRepository.findById(accountId))
                .thenReturn(Optional.empty());
        assertThrows(BankException.class, () -> clientBankService.createBalanceAccount(operationDTO));
        verify(accountBankRepository, times(1)).findById(accountId);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("вывод баланса счета")
    public void getBalanceAccountByIdTest() {
        UUID accountId = UUID.randomUUID();
        AccountBankEntity accountBank = AccountBankEntity.builder()
                .id(accountId)
                .balance(new BigDecimal(5000))
                .status(true)
                .build();
        BalanceResponseDTO expected = BalanceResponseDTO.builder()
                .id(accountId)
                .balance(new BigDecimal(5000))
                .build();
        when(accountBankRepository.findById(accountId))
                .thenReturn(Optional.of(accountBank));
        BalanceResponseDTO actual = clientBankService.getBalanceAccountById(accountId);
        assertEquals(expected.getBalance(), actual.getBalance());
    }

    @Test
    @DisplayName("вывод баланса счета при не найденом id счета в БД")
    public void getBalanceAccountByIdTestThrows() {
        UUID accountId = UUID.randomUUID();
        when(accountBankRepository.findById(accountId))
                .thenReturn(Optional.empty());
        assertThrows(BankException.class, () -> clientBankService.getBalanceAccountById(accountId));
    }

    @Test
    @DisplayName("перевод средств с одного счета клиента1 на счет клиента2 в одинаковых или разных валютах счетов")
    public void transferMoneyCurrencyTest() {
        UUID fromAccountID = UUID.randomUUID();
        UUID toAccountID = UUID.randomUUID();
        var transferAmount = new BigDecimal(100);
        var rate = 1.0;
        var fromAccount = AccountBankEntity.builder()
                .id(fromAccountID)
                .balance(new BigDecimal(1000))
                .build();
        var toAccount = AccountBankEntity.builder()
                .id(toAccountID)
                .balance(new BigDecimal(700))
                .build();
        var transferCurrency = TransferCurrencyRequestDTO.builder()
                .fromAmountID(fromAccountID)
                .toAmountID(toAccountID)
                .fromCurrency("RUB")
                .toCurrency("RUB")
                .amount(transferAmount)
                .build();
        var expectedFromAccount = AccountBankEntity.builder()
                .id(fromAccountID)
                .balance(new BigDecimal(900))
                .build();
        var expectedToAccount = AccountBankEntity.builder()
                .id(fromAccountID)
                .balance(new BigDecimal(800))
                .build();
        when(accountBankRepository.findById(fromAccountID)).thenReturn(Optional.of(fromAccount));
        when(accountBankRepository.findById(toAccountID)).thenReturn(Optional.of(toAccount));
        when(currencyConverterBankServer.convertCurrency(transferAmount, "RUB", "RUB"))
                .thenReturn(rate);
        clientBankService.transferMoneyCurrency(transferCurrency);
        assertEquals(expectedFromAccount.getBalance(), fromAccount.getBalance());
        assertEquals(expectedToAccount.getBalance(), toAccount.getBalance());
        verify(currencyConverterBankServer, times(1)).convertCurrency(transferAmount, "RUB", "RUB");
        verify(accountBankRepository, times(1)).findById(fromAccountID);
        verify(accountBankRepository, times(1)).findById(toAccountID);
        verify(operationBankRepository, times(2)).save(any(OperationBankEntity.class));
    }

    @Test
    @DisplayName("перевод средств с одного счета клиента1 на счет клиента2 при не найденом id счета в БД")
    public void transferMoneyCurrencyTestNoIdThrow() {
        UUID fromAccountID = UUID.randomUUID();
        UUID toAccountID = UUID.randomUUID();
        var transferCurrency = TransferCurrencyRequestDTO.builder()
                .fromAmountID(fromAccountID)
                .toAmountID(toAccountID)
                .fromCurrency("RUB")
                .toCurrency("RUB")
                .amount(new BigDecimal(100))
                .build();
        when(accountBankRepository.findById(fromAccountID)).thenReturn(Optional.empty());
        when(accountBankRepository.findById(toAccountID)).thenReturn(Optional.empty());
        assertThrows(BankException.class, () -> clientBankService.transferMoneyCurrency(transferCurrency));
    }

    @Test
    @DisplayName("перевод средств с одного счета клиента1 на счет клиента2 когда не хватает средств")
    public void transferMoneyCurrencyTestNoMoneyThrow() {
        UUID fromAccountID = UUID.randomUUID();
        UUID toAccountID = UUID.randomUUID();
        var transferAmount = new BigDecimal(2000);
        var rate = 1.0;
        var fromAccount = AccountBankEntity.builder()
                .id(fromAccountID)
                .balance(new BigDecimal(1000))
                .build();
        var toAccount = AccountBankEntity.builder()
                .id(toAccountID)
                .balance(new BigDecimal(700))
                .build();
        var transferCurrency = TransferCurrencyRequestDTO.builder()
                .fromAmountID(fromAccountID)
                .toAmountID(toAccountID)
                .fromCurrency("RUB")
                .toCurrency("RUB")
                .amount(transferAmount)
                .build();
        when(accountBankRepository.findById(fromAccountID)).thenReturn(Optional.of(fromAccount));
        when(accountBankRepository.findById(toAccountID)).thenReturn(Optional.of(toAccount));
        when(currencyConverterBankServer.convertCurrency(transferAmount, "RUB", "RUB"))
                .thenReturn(rate);
        assertThrows(BankException.class, () -> clientBankService.transferMoneyCurrency(transferCurrency));
    }

}
