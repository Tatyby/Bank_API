package com.sberStart.bank_api.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TransferCurrencyRequestDTO {
    @Schema(description = "id счета, откуда должен поступить платеж")
    @NotNull
    private UUID fromAmountID;
    @NotNull
    @Schema(description = "id счета, куда должен поступить платеж")
    private UUID toAmountID;
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Schema(description = "Сумма платежа")
    private BigDecimal amount;
    @NotBlank
    @Schema(description = "Валюта счета, откуда должен поступить платеж")
    private String fromCurrency;
    @NotBlank
    @Schema(description = "Валюта счета, куда должен поступить платеж")
    private String toCurrency;
}
