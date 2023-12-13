package com.sberStart.bank_api.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OperationRequestDTO {
    @NotNull
    @Schema(description = "id счета, куда нужно перевести деньги")
    private UUID id;
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Schema(description = "Сумма платежа")
    private BigDecimal amount;
    @Schema(description = "Статус подтверждения банком операции перевода")
    @NotNull
    private Boolean bankConfirmationStatus;
}
