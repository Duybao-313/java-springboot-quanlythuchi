package com.duybao.QUANLYCHITIEU.DTO.request;


import com.duybao.QUANLYCHITIEU.Enum.BudgetStatus;
import com.duybao.QUANLYCHITIEU.Enum.PeriodType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateBudgetRequest {

    @NotBlank(message = "name must not be blank")
    private String name;

    @DecimalMin(value = "0.01", inclusive = true, message = "amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "startDate is required")
    private LocalDate startDate;

    @NotNull(message = "endDate is required")
    private LocalDate endDate;

    private PeriodType periodType; // ONE_TIME, MONTHLY, WEEKLY

    @Valid
    private List<ScopeDto> scopes;

    private BudgetStatus budgetStatus;

    @Valid
    private List<ThresholdDto> thresholds;
}