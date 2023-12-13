package com.sberStart.bank_api.controller;

import com.sberStart.bank_api.dto.client.AccountResponseDTO;
import com.sberStart.bank_api.dto.client.BalanceResponseDTO;
import com.sberStart.bank_api.dto.client.OperationRequestDTO;
import com.sberStart.bank_api.dto.client.OperationResponseDTO;
import com.sberStart.bank_api.dto.client.TransferCurrencyRequestDTO;
import com.sberStart.bank_api.service.client.ClientBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/bank/client")
@RequiredArgsConstructor
@Validated
public class ClientBankController {
    private final ClientBankService bankService;

    @Tag(name = "Клиент", description = "позволяет посмотреть список счетов у клиента")
    @Operation(summary = "Список счетов")
    @GetMapping("/accounts/{idUser}")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts(@PathVariable @NotNull @Parameter(description = "id клиента") UUID idUser) {
        return ResponseEntity.status(HttpStatus.OK).body(bankService.getAllAccounts(idUser));
    }

    @Tag(name = "Клиент", description = "позволяет пополнить баланс счета у клиента")
    @Operation(summary = "Пополнение счета")
    @PostMapping("/depositMoneyToAccount")
    public ResponseEntity<OperationResponseDTO> createBalanceAccount(@Validated @RequestBody OperationRequestDTO operationRequestDTO) {
        return new ResponseEntity<>(bankService.createBalanceAccount(operationRequestDTO), HttpStatus.CREATED);

    }

    @Tag(name = "Клиент", description = "позволяет вывести баланс счета у клиента")
    @Operation(summary = "Баланс счета")
    @GetMapping("/balance/{idAccount}")
    public ResponseEntity<BalanceResponseDTO> getBalanceAccountById(@PathVariable @NotNull @Parameter(description = "id счета") UUID idAccount) {
        return new ResponseEntity<>(bankService.getBalanceAccountById(idAccount), HttpStatus.OK);
    }

    @Tag(name = "Клиент", description = "позволяет перевести средства со счета клиента1 на счет клиента2 в одинаковых валютах или разных")
    @Operation(summary = "Перевод средств")
    @PostMapping("/transferCurrency")
    public ResponseEntity<?> transferMoneyCurrency(@Validated @RequestBody TransferCurrencyRequestDTO transferCurrency) {
        bankService.transferMoneyCurrency(transferCurrency);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Список операций по id счета")
    @Tag(name = "Клиент", description = "Позволяет посмотреть список операций по id счета")
    @GetMapping("/operation/{idAccount}")
    public ResponseEntity<List<OperationResponseDTO>> getOperationByIdAccount(@PathVariable UUID idAccount) {
        return new ResponseEntity<>(bankService.getOperationByIdAccount(idAccount), HttpStatus.OK);
    }

}
