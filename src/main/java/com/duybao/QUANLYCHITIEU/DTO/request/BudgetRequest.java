package com.duybao.QUANLYCHITIEU.DTO.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BudgetRequest {
    private BigDecimal amountLimit;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long categoryId;
}
