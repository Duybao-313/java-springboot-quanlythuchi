package com.duybao.QUANLYCHITIEU.Service;


import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.CreateBudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.BudgetRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.CreateBudgetRequest;

import java.util.List;

public interface BudgetService {
    public CreateBudgetResponse createBudget(CreateBudgetRequest req, Long actorUserId);

    public List<BudgetResponse> getBudgets(Long userId);

    public BudgetResponse updateBudget(Long userId, Long id, BudgetRequest request) ;

    public void deleteBudget(Long userId, Long id);
}
