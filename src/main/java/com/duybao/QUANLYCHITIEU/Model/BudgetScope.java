package com.duybao.QUANLYCHITIEU.Model;


import com.duybao.QUANLYCHITIEU.Enum.ScopeType;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "budget_scope")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BudgetScope {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @Column(name = "scope_type", length = 20, nullable = false)
    private ScopeType scopeType; // CATEGORY, ACCOUNT, TAG, WALLET

    @Column(name = "ref_id", nullable = false)
    private Long refId;
}