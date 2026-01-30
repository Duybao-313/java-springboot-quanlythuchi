package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Model.Budget;
import com.duybao.QUANLYCHITIEU.Model.BudgetUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BudgetUsageRepository extends JpaRepository<BudgetUsage, Long> {
    Optional<BudgetUsage> findByBudgetIdAndPeriodStartAndPeriodEnd(Long budgetId, java.time.LocalDate periodStart, java.time.LocalDate periodEnd);
    List<BudgetUsage> findByBudgetId(Long budgetId);

    Optional<BudgetUsage> findByBudgetAndPeriodStartAndPeriodEnd(Budget budget, LocalDate periodStart, LocalDate periodEnd);
    @Modifying
    @Query("UPDATE BudgetUsage u SET u.spentAmount = u.spentAmount + :amount, u.lastUpdated = CURRENT_TIMESTAMP WHERE u.id = :id")
    int incrementSpent(Long id, BigDecimal amount);

    @Modifying
    @Query("UPDATE BudgetUsage u SET u.spentAmount = u.spentAmount - :amount, u.lastUpdated = CURRENT_TIMESTAMP WHERE u.id = :id")
    int decrementSpent(Long id, BigDecimal amount);

}
