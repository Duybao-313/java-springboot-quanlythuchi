package com.duybao.QUANLYCHITIEU.DTO.Response.budget;

import com.duybao.QUANLYCHITIEU.Enum.PeriodType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BudgetResponse {
    private Long id;
    private String name;
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType periodType;
}