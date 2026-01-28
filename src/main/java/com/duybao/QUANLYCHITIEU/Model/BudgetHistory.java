package com.duybao.QUANLYCHITIEU.Model;


import com.duybao.QUANLYCHITIEU.Enum.BudgetChangeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "budget_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BudgetHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @Column(name = "changed_by")
    private Long changedBy;

    @Column(name = "note")
    private String note;

    @Column(name = "change_type", length = 50)
    private BudgetChangeType changeType;

    @Column(name = "old_value_json", columnDefinition = "TEXT")
    private String oldValueJson;

    @Column(name = "new_value_json", columnDefinition = "TEXT")
    private String newValueJson;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}