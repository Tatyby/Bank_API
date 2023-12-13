package com.sberStart.bank_api.service.client;

import com.sberStart.bank_api.dto.client.AccountResponseDTO;
import com.sberStart.bank_api.dto.client.BalanceResponseDTO;
import com.sberStart.bank_api.dto.client.OperationRequestDTO;
import com.sberStart.bank_api.dto.client.OperationResponseDTO;
import com.sberStart.bank_api.dto.client.TransferCurrencyRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ClientBankService {

    List<AccountResponseDTO> getAllAccounts(UUID idUser);

    OperationResponseDTO createBalanceAccount(OperationRequestDTO operation);

    BalanceResponseDTO getBalanceAccountById(UUID idAccount);

    void transferMoneyCurrency(TransferCurrencyRequestDTO transferCurrency);

    List<OperationResponseDTO> getOperationByIdAccount(UUID idAccount);
}
