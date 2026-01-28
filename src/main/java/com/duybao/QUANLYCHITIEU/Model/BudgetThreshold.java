package com.duybao.QUANLYCHITIEU.Model;


import com.duybao.QUANLYCHITIEU.Enum.BudgetAction;
import jakarta.persistence.*;
import lombok.*;



import java.time.LocalDateTime;

@Entity
@Table(name = "budget_threshold")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BudgetThreshold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @Column(nullable = false)
    private Integer percent;

    @Column(length = 20, nullable = false)
    private BudgetAction action; // NOTIFY, BLOCK

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}