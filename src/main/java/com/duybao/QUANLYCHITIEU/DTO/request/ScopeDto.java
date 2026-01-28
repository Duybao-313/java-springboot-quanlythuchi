package com.duybao.QUANLYCHITIEU.DTO.request;

import com.duybao.QUANLYCHITIEU.Enum.ScopeType;
import lombok.Data;

@Data
public class ScopeDto {
    private ScopeType scopeType; // CATEGORY, WALLET, ACCOUNT, TAG
    private Long refId;
}