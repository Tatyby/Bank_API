package com.sberStart.bank_api.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ClientAdminResponseDTO {
    @Schema(description = "уникальный id клиента")
    private UUID id;
    @Schema(description = "Имя клиента")
    private String firstName;
    @Schema(description = "Фамилия клиента")
    private String lastName;
    @Schema(description = "Телефонный номер клиента")
    private String phoneNumber;
    @Schema(description = "email клиента")
    private String email;
    @Schema(description = "Время создания клиента в БД")
    private LocalDateTime createdTime;
    @Schema(description = "Время изменения данных клиента")
    private LocalDateTime modificationTime;
}
