package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.CreateBudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.CreateBudgetRequest;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.DTO.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.BudgetRequest;
import com.duybao.QUANLYCHITIEU.Service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;
    @PostMapping
    public ApiResponse<CreateBudgetResponse> createBudget(@RequestBody CreateBudgetRequest request,
                                                          @AuthenticationPrincipal User user) {
        return ApiResponse.<CreateBudgetResponse>builder()
                .message("Tạo ngân sách thành công")
                .data(budgetService.createBudget( request, user.getId()))
                .success(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }



    @PutMapping("/{id}")
    public ApiResponse<BudgetResponse> updateBudget(@PathVariable Long id,
                                                    @RequestBody BudgetRequest request,
                                                    @AuthenticationPrincipal User userDetails) {
        return ApiResponse.<BudgetResponse>builder()
                .message("Cập nhật ngân sách thành công")
                .data(budgetService.updateBudget(userDetails.getId(), id, request))
                .success(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBudget(@PathVariable Long id,
                                          @AuthenticationPrincipal User userDetails) {
        budgetService.deleteBudget(userDetails.getId(), id);
        return ApiResponse.<Void>builder()
                .message("Xóa ngân sách thành công")
                .success(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @GetMapping
    public ApiResponse<Page<BudgetDto>> getMyBudgets(
            @PageableDefault(size = 20, sort = "startDate", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user) {
        Page<BudgetDto> page = budgetService.getBudgetsForCurrentUser(pageable,user.getId());
         return ApiResponse.<Page<BudgetDto>>builder()
                .message("Lấy ngân sách thành công")
                .success(true)
                 .data(page)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }



    // GET single budget for current user
    @GetMapping("/{id}")
    public ApiResponse<BudgetDto> getBudget(@PathVariable Long id,@AuthenticationPrincipal User user) {


        BudgetDto dto = budgetService.getBudgetByIdForUser(id, user.getId());
        return ApiResponse.<BudgetDto>builder()
                .message("Lấy ngân sách thành công")
                .success(true)
                .data(dto)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ApiResponse<Page<BudgetDto>> getBudgetsForUser(
            @PathVariable Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<BudgetDto> page = budgetService.getBudgetsForUser(userId, pageable);
        return ApiResponse.<Page<BudgetDto>>builder()
                .message("Lấy ngân sách thành công")
                .success(true)
                .data(page)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

}