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
    // --- trigger fields ---
    @Builder.Default
    @Column(nullable = false)
    private Boolean triggered = false;

    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt;

    @Column(name = "triggered_by", length = 50)
    private String triggeredBy; // e.g., SYSTEM, USER, SCHEDULE

    @Column(name = "triggered_usage_id")
    private Long triggeredUsageId;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}