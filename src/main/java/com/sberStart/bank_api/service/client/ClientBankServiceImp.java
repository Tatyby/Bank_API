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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientBankServiceImp implements ClientBankService {
    private final AccountBankRepository accountBankRepository;
    private final ClientBankRepository clientBankRepository;
    private final ModelMapper mapper;
    private final OperationBankRepository operationBankRepository;
    private final CurrencyConverterBankServerImp currencyConverterBankServer;

    /**
     * получение списка счетов по id клиента
     *
     * @param idUser id клиента
     * @return список сущностей(счетов), состоящих из idAccount(id счета), numberAccount(номер счета)
     * @throws BankException ошибка 404, если клиент по id не был найден в БД
     */

    @Transactional(readOnly = true)
    @Override
    public List<AccountResponseDTO> getAllAccounts(UUID idUser) {
        Optional<ClientBankEntity> clientBankEntityOptional = clientBankRepository.findById(idUser);
        if (clientBankEntityOptional.isEmpty()) {
            throw new BankException("ID_USER " + idUser + " не найдено в БД");
        }
        ClientBankEntity clientBankEntity = clientBankEntityOptional.get();
        List<AccountBankEntity> listAccountBankEntity = clientBankEntity.getAccount();
        return listAccountBankEntity.stream()
                .map(x -> mapper.map(x, AccountResponseDTO.class))
                .toList();
    }

    /**
     * пополнение счета по его id
     *
     * @param operation сущность, состоящая из id (счета) и amount (сумма платежа)
     * @return сущность, состоящая из id(операции) и amount (сумма платежа)
     * @throws BankException ошибка 404, если счет по id не был найден в БД
     */
    @Transactional
    @Override
    public OperationResponseDTO createBalanceAccount(OperationRequestDTO operation) {

        Optional<AccountBankEntity> account = accountBankRepository.findById(operation.getId());
        if (account.isEmpty()) {
            throw new BankException("ID_ACCOUNT " + operation.getId() + " не найдено в БД");
        }
        if (!account.get().getStatus()) {
            throw new BankException("Статус счета с ID_ACCOUNT " + operation.getId() + " заблокирован, перевод средств невозможен");
        }
        if (!operation.getBankConfirmationStatus()) {
            throw new BankException("Операция пополнения имеет статус не подтвержден банком, перевод невозможен");
        }
        account.get().setBalance(operation.getAmount().add(account.get().getBalance()));
        accountBankRepository.save(account.get());
        OperationBankEntity operationBankEntity = operationBankRepository.save(OperationBankEntity.builder()
                .amount(operation.getAmount())
                .account(account.get())
                .bankConfirmationStatus(true)
                .build());
        return mapper.map(operationBankEntity, OperationResponseDTO.class);
    }

    /**
     * вывод баланса счета
     *
     * @param idAccount id счета, по которому нужно вывести баланс
     * @return сущность, состоящая из id счета и balance (баланс счета)
     * @throws BankException ошибка 404, если счет по id не был найден в БД
     */
    @Transactional(readOnly = true)
    @Override
    public BalanceResponseDTO getBalanceAccountById(UUID idAccount) {
        Optional<AccountBankEntity> account = accountBankRepository.findById(idAccount);
        if (account.isPresent()) {
            BigDecimal balance = account.get().getBalance();
            return new BalanceResponseDTO(idAccount, balance);
        } else {
            throw new BankException("ID_ACCOUNT " + idAccount + " не найдено в БД");
        }

    }

    /**
     * перевод средств с одного счета клиента1 на счет клиента2 в одинаковых или разных валютах счетов
     *
     * @param transferCurrency сущность, состоящая из fromAmountID(откуда нужно произвести платеж,
     *                         toAmountID(куда нужно зачислить платеж),
     *                         amount(сумма платежа),
     *                         fromCurrency(валюта счета, откуда списываются деньги,
     *                         toCurrency(валюта счета, куда поступают деньги)
     */
    @Override
    @Transactional
    public void transferMoneyCurrency(TransferCurrencyRequestDTO transferCurrency) {
        double resultConvert =
                currencyConverterBankServer.convertCurrency(transferCurrency.getAmount(),
                        transferCurrency.getFromCurrency(), transferCurrency.getToCurrency());
        Optional<AccountBankEntity> fromAccount = accountBankRepository.findById(transferCurrency.getFromAmountID());
        Optional<AccountBankEntity> toAccount = accountBankRepository.findById(transferCurrency.getToAmountID());
        if (!(fromAccount.isPresent() && toAccount.isPresent())) {
            throw new BankException("ID_ACCOUNT не найдено в БД");
        }
        BigDecimal fromBalance = fromAccount.get().getBalance();
        BigDecimal amount = transferCurrency.getAmount();
        if (fromBalance.compareTo(amount) < 0) {
            throw new BankException("На счете: " + transferCurrency.getFromAmountID() + " недостаточно средств");
        }
        fromAccount.get().setBalance(fromBalance.subtract(amount));
        toAccount.get().setBalance(toAccount.get().getBalance().add(amount));
        operationBankRepository.save(OperationBankEntity.builder()
                .amount(transferCurrency.getAmount().multiply(new BigDecimal(-1)))
                .account(fromAccount.get())
                .build());
        operationBankRepository.save(OperationBankEntity.builder()
                .amount(transferCurrency.getAmount().multiply(BigDecimal.valueOf(resultConvert)))
                .account(toAccount.get())
                .build());
    }

    @Override
    @Transactional
    public List<OperationResponseDTO> getOperationByIdAccount(UUID idAccount) {
        Optional<AccountBankEntity> accountBankEntityOptional = accountBankRepository.findById(idAccount);
        if (accountBankEntityOptional.isEmpty()) {
            throw new BankException("ID_ACCOUNT " + idAccount + " не найдено в БД");
        }
        AccountBankEntity accountBankEntity = accountBankEntityOptional.get();
        List<OperationBankEntity> operationBankEntityList = accountBankEntity.getOperation();
        return operationBankEntityList.stream()
                .map(x -> mapper.map(x, OperationResponseDTO.class))
                .toList();
    }
}
