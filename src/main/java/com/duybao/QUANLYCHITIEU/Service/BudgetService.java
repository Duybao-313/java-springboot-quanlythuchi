package com.duybao.QUANLYCHITIEU.Service;


import com.duybao.QUANLYCHITIEU.Response.budget.BudgetResponse;
import com.duybao.QUANLYCHITIEU.Response.budget.request.BudgetRequest;

import java.util.List;

public interface BudgetService {
    public BudgetResponse createBudget(Long userId, BudgetRequest request);

    public List<BudgetResponse> getBudgets(Long userId);

    public BudgetResponse updateBudget(Long userId, Long id, BudgetRequest request) ;

    public void deleteBudget(Long userId, Long id);
}
