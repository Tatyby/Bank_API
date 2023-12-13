package com.sberStart.bank_api.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationResponseDTO {
    @Schema(description = "Уникальный id операции по счету")
    private UUID id;
    @Schema(description = "Сумма платежа")
    private BigDecimal amount;
}


