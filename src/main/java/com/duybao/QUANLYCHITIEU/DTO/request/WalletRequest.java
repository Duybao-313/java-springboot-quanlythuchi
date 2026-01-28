package com.duybao.QUANLYCHITIEU.DTO.request;

import com.duybao.QUANLYCHITIEU.Enum.WalletType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletRequest {
    @NotBlank(message = "NAME_NOT_NULL")
    @Size(max = 60,message = "NAME_TOO_LONG")
    private String name;

    @NotNull
    @DecimalMin(value = "1.1",message = "AMOUNT_NOT_NEGATIVE")
    private BigDecimal balance;

    @NotNull(message = "TYPE_NOT_NULL")
    private WalletType type;

    private String description;
}
