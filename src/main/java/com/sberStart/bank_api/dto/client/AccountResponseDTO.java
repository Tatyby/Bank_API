package com.sberStart.bank_api.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    @Schema(description = "Уникальный id счета")
    private UUID idAccount;
    @Schema(description = "Номер счета")
    private String numberAccount;
}
