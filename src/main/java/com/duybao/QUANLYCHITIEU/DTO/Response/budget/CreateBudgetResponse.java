package com.duybao.QUANLYCHITIEU.DTO.Response.budget;

import com.duybao.QUANLYCHITIEU.Enum.BudgetStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateBudgetResponse {
    private Long id;
    private BudgetStatus status;
}