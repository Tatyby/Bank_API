package com.sberStart.bank_api.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalanceResponseDTO {
    @Schema(description = "Уникальный id счета")
    private UUID id;
    @Schema(description = "Баланс счета")
    private BigDecimal balance;
}
