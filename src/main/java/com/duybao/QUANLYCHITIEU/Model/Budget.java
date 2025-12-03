package com.duybao.QUANLYCHITIEU.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amountLimit;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    private User user;                // ngân sách thuộc user nào

    @ManyToOne
    private Category category;        // ngân sách cho danh mục nào (ăn uống, mua sắm...)
}