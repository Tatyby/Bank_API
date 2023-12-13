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
@Schema(description = "Сущность нового клиента")
public class NewClientAdminRequestDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;
    @NotBlank
    @Schema(description = "Имя клиента")
    private String firstName;
    @NotBlank
    @Schema(description = "Фамилия клиента")
    private String lastName;
    @NotBlank
    @Schema(description = "Телефонный номер")
    @Pattern(regexp = "\\+\\d{1,3}-\\d{1,14}", message = "Некорректный формат телефонного номера")
    private String phoneNumber;
    @NotBlank
    @Schema(description = "email клиента")
    @Email
    private String email;

}
