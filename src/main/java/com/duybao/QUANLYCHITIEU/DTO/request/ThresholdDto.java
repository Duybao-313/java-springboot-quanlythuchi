package com.duybao.QUANLYCHITIEU.DTO.request;

import com.duybao.QUANLYCHITIEU.Enum.BudgetAction;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThresholdDto {
    @NotNull(message = "percent is required")
    @Min(value = 0, message = "percent must be between 0 and 100")
    @Max(value = 100, message = "percent must be between 0 and 100")
    private Integer percent;
    @NotNull(message = "action is required")
    private BudgetAction action; // NOTIFY, BLOCK
}