package com.duybao.QUANLYCHITIEU.Response.budget;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BudgetResponse {
    private Long id;
    private BigDecimal amountLimit;
    private LocalDate startDate;
    private LocalDate endDate;
    private String categoryName;
}