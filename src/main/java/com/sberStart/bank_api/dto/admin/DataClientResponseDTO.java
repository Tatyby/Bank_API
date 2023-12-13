package com.sberStart.bank_api.dto.admin;

import com.sberStart.bank_api.dto.client.AccountResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class DataClientResponseDTO {
    @Schema(description = "Уникальный id клиента")
    private UUID id;
    @Schema(description = "Имя клиента")
    private String firstName;
    @Schema(description = "Фамилия клиента")
    private String lastName;
    @Schema(description = "Телефонный номер клиента")
    private String phoneNumber;
    @Schema(description = "email клиента")
    private String email;
    @Schema(description = "Список счетов клиента")
    private List<AccountResponseDTO> accountList;
}
