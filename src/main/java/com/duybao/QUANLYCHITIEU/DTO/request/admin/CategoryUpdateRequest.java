package com.duybao.QUANLYCHITIEU.DTO.request.admin;

import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.text.Normalizer;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryUpdateRequest {
    private Long id;
    @NotBlank(message = "Tên danh mục không đưọc để trống")
    private String name;
    @NotNull(message = "Loại không đưọc để trống")
    private TransactionType type;
}
