package com.duybao.QUANLYCHITIEU.DTO.Response.Wallet;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletOverview {
    private BigDecimal totalBalance;
    private Integer walletCount;
    private BigDecimal totalExpense;
    private Long ExpenseCount;
    private Long IncomeCount;
    private BigDecimal totalIncome;
    private BigDecimal netBalance;
}
