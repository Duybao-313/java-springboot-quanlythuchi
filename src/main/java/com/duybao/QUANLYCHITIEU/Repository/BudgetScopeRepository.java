package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Enum.ScopeType;
import com.duybao.QUANLYCHITIEU.Model.BudgetScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetScopeRepository extends JpaRepository<BudgetScope, Long> {
    List<BudgetScope> findByRefIdAndScopeType(Long refId, ScopeType scopeType);
    List<BudgetScope> findByBudgetId(Long budgetId);
}
