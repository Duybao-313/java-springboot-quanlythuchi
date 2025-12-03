package com.duybao.QUANLYCHITIEU.Response.Wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletResponse {
    private Long id;
    private String name;
    private BigDecimal balance;
    private String type;
    private String iconUrl;
    private String description;
    private LocalDateTime createdAt;
}
