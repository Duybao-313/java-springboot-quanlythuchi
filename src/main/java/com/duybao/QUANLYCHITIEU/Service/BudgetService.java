package com.duybao.QUANLYCHITIEU.Service;


import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetDetailResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.CreateBudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.BudgetRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.CreateBudgetRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.UpdateBudgetRequest;
import com.duybao.QUANLYCHITIEU.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BudgetService {
    public CreateBudgetResponse createBudget(CreateBudgetRequest req, Long actorUserId);


    public void updateBudget(User user, Long id, UpdateBudgetRequest request) ;

    public void deleteBudget(Long userId, Long id);
    Page<BudgetDto> getBudgetsForCurrentUser(Pageable pageable,Long userId);
    Page<BudgetDto> getBudgetsForUser(Long userId, Pageable pageable);
    BudgetDetailResponse getBudgetByIdForUser(Long budgetId, Long userId);
}
