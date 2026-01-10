package com.duybao.QUANLYCHITIEU.DTO.request;

import com.duybao.QUANLYCHITIEU.Enum.WalletType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletRequest {
    @NotBlank
    private String name;

    @NotNull
    private BigDecimal balance;

    @NotNull
    private WalletType type;

    private String iconUrl;
    private String description;
}
