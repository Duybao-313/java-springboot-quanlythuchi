package com.duybao.QUANLYCHITIEU.Response.Transaction;

import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private String categoryName;
    private String walletName;
}
