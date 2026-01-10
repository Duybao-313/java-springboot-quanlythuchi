package com.duybao.QUANLYCHITIEU.DTO.Response.category;

import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private TransactionType type;   // EXPENSE hoặc INCOME
    private String iconUrl;
    private String color;
    private Long ownerId;
}
