package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Model.BudgetTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetTransactionRepository extends JpaRepository<BudgetTransaction, Long> {
    boolean existsByBudgetIdAndTransactionId(Long budgetId, Long transactionId);

    void deleteByBudgetId(Long BudgetId);
}