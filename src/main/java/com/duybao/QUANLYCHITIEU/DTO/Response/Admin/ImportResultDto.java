package com.duybao.QUANLYCHITIEU.DTO.Response.Admin;

import lombok.*;
import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor
public class ImportResultDto {
    private int total;
    private int successCount;
    private List<RowError> errors;
}