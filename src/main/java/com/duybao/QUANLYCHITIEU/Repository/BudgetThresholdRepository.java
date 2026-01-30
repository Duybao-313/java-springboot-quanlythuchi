package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Model.BudgetThreshold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetThresholdRepository extends JpaRepository<BudgetThreshold, Long> {
    List<BudgetThreshold> findByBudgetId(Long budgetId);

    void deleteByBudgetId(Long budgetId);
}
