package com.duybao.QUANLYCHITIEU.DTO.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferRequest {
    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "1.1",message = "AMOUNT_NOT_NEGATIVE")
    private BigDecimal amount;

    private String description;

    @NotNull(message = "Ví không được để trống")
    private Long walletIdTransfer;
    @NotNull(message = "Ví không được để trống")
    private Long walletIdReceive;
}