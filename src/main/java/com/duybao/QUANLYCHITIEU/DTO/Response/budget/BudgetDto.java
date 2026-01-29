package com.duybao.QUANLYCHITIEU.DTO.Response.budget;

import com.duybao.QUANLYCHITIEU.Enum.BudgetStatus;
import com.duybao.QUANLYCHITIEU.Enum.PeriodType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

// BudgetDto.java
@Data
@Builder
public class BudgetDto {
    private Long id;
    private String name;
    private Long ownerId;
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType periodType;
    private BudgetStatus status;
}