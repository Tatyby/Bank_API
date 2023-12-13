package com.sberStart.bank_api.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class OpenAccountRequestDTO {
    @Schema(description = "id клиента, у которого нужно открыть счет")
    private UUID id;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String numberAccount;
    @NotNull
    @Schema(description = "Статус счета клиента")
    private Boolean status;
    @NotBlank
    @Schema(description = "Валюта счета клиента")
    private String currency;
}
