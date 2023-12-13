package com.sberStart.bank_api.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class OpenAccountResponseDTO {
    @Schema(description = "Уникальный id счета клиента")
    private UUID id;
    @Schema(description = "Номер счета клиента")
    private String numberAccount;
}
