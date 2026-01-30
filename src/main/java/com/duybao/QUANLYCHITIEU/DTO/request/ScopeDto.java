package com.duybao.QUANLYCHITIEU.DTO.request;

import com.duybao.QUANLYCHITIEU.Enum.ScopeType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScopeDto {
    @NotNull(message = "scopeType is required")
    private ScopeType scopeType;
    @NotNull(message = "refId is required")
    @Min(value = 1, message = "refId must be positive")
    private Long refId;
}