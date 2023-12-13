package com.sberStart.bank_api.controller;

import com.sberStart.bank_api.dto.admin.ClientAdminRequestDTO;
import com.sberStart.bank_api.dto.admin.ClientAdminResponseDTO;
import com.sberStart.bank_api.dto.admin.DataClientResponseDTO;
import com.sberStart.bank_api.dto.admin.NewClientAdminRequestDTO;
import com.sberStart.bank_api.dto.admin.OpenAccountRequestDTO;
import com.sberStart.bank_api.dto.admin.OpenAccountResponseDTO;
import com.sberStart.bank_api.service.admin.AdminBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/bank/admin")
@RequiredArgsConstructor
@Validated
public class AdminBankController {
    private final AdminBankService adminBankServiceImp;

    @Tag(name = "Админ", description = "позволяет создать новых клиентов банка")
    @Operation(summary = "Создание нового клиента банка")
    @PostMapping("/newClient")
    public ResponseEntity<ClientAdminResponseDTO> createNewClient(@Validated @RequestBody NewClientAdminRequestDTO client) {
        return new ResponseEntity<>(adminBankServiceImp.createNewClient(client), HttpStatus.CREATED);
    }

    @Tag(name = "Админ", description = "позволяет обновить данные клиента банка")
    @Operation(summary = "Обновление данных клиента банка")
    @PatchMapping("/update")
    public ResponseEntity<ClientAdminResponseDTO> updateClient(@Validated @RequestBody ClientAdminRequestDTO client) {
        return new ResponseEntity<>(adminBankServiceImp.updateClient(client), HttpStatus.CREATED);
    }

    @Tag(name = "Админ", description = "позволяет посмотреть всю информацию у клиента")
    @Operation(summary = "Информация по клиенту банка")
    @GetMapping("/dataClient/{idClient}")
    public ResponseEntity<DataClientResponseDTO> getDataClient(@PathVariable @NotNull @Parameter(description = "id клиента") UUID idClient) {
        return new ResponseEntity<>(adminBankServiceImp.getDataClient(idClient), HttpStatus.OK);
    }

    @Tag(name = "Админ", description = "позволяет открыть новый счет у клиента")
    @Operation(summary = "Открытие нового счета")
    @PostMapping("/openAccount")
    public ResponseEntity<OpenAccountResponseDTO> openAccountByIdClient(
            @Validated @RequestBody OpenAccountRequestDTO openAccount) {
        return new ResponseEntity<>(adminBankServiceImp.openAccountByIdClient(openAccount), HttpStatus.CREATED);
    }


}
