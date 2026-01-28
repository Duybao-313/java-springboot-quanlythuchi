package com.duybao.QUANLYCHITIEU.DTO.request;

import com.duybao.QUANLYCHITIEU.Enum.BudgetAction;
import lombok.Data;

@Data
public class ThresholdDto {
    private Integer percent;
    private BudgetAction action; // NOTIFY, BLOCK
}