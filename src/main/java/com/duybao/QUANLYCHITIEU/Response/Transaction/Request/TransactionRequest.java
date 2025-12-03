package com.duybao.QUANLYCHITIEU.Response.Transaction.Request;

import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRequest {
    @NotNull
    private BigDecimal amount;

    @NotNull
    private TransactionType type;

    private String description;

    @NotNull
    private Long walletId;

    @NotNull
    private Long categoryId;
}