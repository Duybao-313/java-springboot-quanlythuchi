package com.duybao.QUANLYCHITIEU.DTO.request;

import com.duybao.QUANLYCHITIEU.Enum.BudgetStatus;
import com.duybao.QUANLYCHITIEU.Enum.PeriodType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateBudgetRequest {
    private String name;
    private BigDecimal amount;
    private String currency;
    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType periodType; // ONE_TIME, MONTHLY, WEEKLY
    private List<ScopeDto> scopes;
    private BudgetStatus budgetStatus;
    private List<ThresholdDto> thresholds;
}