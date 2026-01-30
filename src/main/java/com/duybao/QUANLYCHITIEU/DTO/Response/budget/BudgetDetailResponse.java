package com.duybao.QUANLYCHITIEU.DTO.Response.budget;

import com.duybao.QUANLYCHITIEU.DTO.request.ScopeDto;
import com.duybao.QUANLYCHITIEU.DTO.request.ThresholdDto;
import com.duybao.QUANLYCHITIEU.Enum.BudgetStatus;
import com.duybao.QUANLYCHITIEU.Enum.PeriodType;
import com.duybao.QUANLYCHITIEU.Model.BudgetScope;
import com.duybao.QUANLYCHITIEU.Model.BudgetThreshold;
import jdk.jfr.Threshold;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class BudgetDetailResponse {
    private String name;
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType periodType; // ONE_TIME, MONTHLY, WEEKLY
    private List<ScopeDto> scopes;
    private BudgetStatus budgetStatus;
    private List<ThresholdDto> thresholds;
}