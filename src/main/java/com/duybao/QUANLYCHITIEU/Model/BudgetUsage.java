package com.duybao.QUANLYCHITIEU.Model;


import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "budget_usage")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BudgetUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    @Column(name = "spent_amount", precision = 18, scale = 2)
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    public void prePersist() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
}