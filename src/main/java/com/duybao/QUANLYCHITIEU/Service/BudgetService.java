package com.duybao.QUANLYCHITIEU.Service;


import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.CreateBudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.BudgetRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.CreateBudgetRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BudgetService {
    public CreateBudgetResponse createBudget(CreateBudgetRequest req, Long actorUserId);

    public List<BudgetResponse> getBudgets(Long userId);

    public BudgetResponse updateBudget(Long userId, Long id, BudgetRequest request) ;

    public void deleteBudget(Long userId, Long id);
    Page<BudgetDto> getBudgetsForCurrentUser(Pageable pageable,Long userId);
    Page<BudgetDto> getBudgetsForUser(Long userId, Pageable pageable);
    BudgetDto getBudgetByIdForUser(Long budgetId, Long userId);
}
