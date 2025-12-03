package com.duybao.QUANLYCHITIEU.Response.Transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private String description;
    private String categoryName;
    private String walletName;
}
