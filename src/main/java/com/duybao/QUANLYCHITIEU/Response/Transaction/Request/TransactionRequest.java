package com.duybao.QUANLYCHITIEU.Response.Transaction.Request;

import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRequest {
    @NotNull(message = "Số tiền không được để trống")
    private BigDecimal amount;

    private String description;

    @NotNull(message = "Ví không được để trống")
    private Long walletId;

    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;
}