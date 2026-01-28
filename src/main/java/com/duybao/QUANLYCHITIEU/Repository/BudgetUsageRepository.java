package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Model.Budget;
import com.duybao.QUANLYCHITIEU.Model.BudgetUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BudgetUsageRepository extends JpaRepository<BudgetUsage, Long> {
    List<BudgetUsage> findByBudgetIdAndPeriodStartAndPeriodEnd(Long budgetId, java.time.LocalDate periodStart, java.time.LocalDate periodEnd);
    List<BudgetUsage> findByBudgetId(Long budgetId);

    Optional<BudgetUsage> findByBudgetAndPeriodStartAndPeriodEnd(Budget budget, LocalDate periodStart, LocalDate periodEnd);
}
