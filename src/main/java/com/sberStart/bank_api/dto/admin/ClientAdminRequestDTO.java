package com.sberStart.bank_api.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Schema(description = "Сущность клиента, которого нужно обновить")
public class ClientAdminRequestDTO {
    @Schema(description = "id клиента, которого нужно обновить")
    private UUID id;
    @NotBlank
    @Schema(description = "Имя клиента")
    private String firstName;
    @NotBlank
    @Schema(description = "Фамилия клиента")
    private String lastName;
    @NotBlank
    @Pattern(regexp = "\\+\\d{1,3}-\\d{1,14}", message = "Некорректный формат телефонного номера")
    @Schema(description = "Телефонный номер")
    private String phoneNumber;
    @NotBlank
    @Schema(description = "email клиента")
    @Email
    private String email;
}