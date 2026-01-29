package com.duybao.QUANLYCHITIEU.Service.Impl;

import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.CreateBudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.CreateBudgetRequest;
import com.duybao.QUANLYCHITIEU.Enum.BudgetChangeType;
import com.duybao.QUANLYCHITIEU.Enum.BudgetStatus;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.BudgetMapper;
import com.duybao.QUANLYCHITIEU.Model.*;
import com.duybao.QUANLYCHITIEU.Repository.*;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.BudgetRequest;
import com.duybao.QUANLYCHITIEU.Service.BudgetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetMapper budgetMapper;
    private final BudgetScopeRepository budgetScopeRepository;
    private final BudgetThresholdRepository budgetThresholdRepository;
    private final BudgetUsageRepository budgetUsageRepository;
    private final BudgetHistoryRepository budgetHistoryRepository;
    private final ObjectMapper objectMapper; // for optional serialization
    private final UserRepository userRepository;



    public List<BudgetResponse> getBudgets(Long userId) {

        return null;
    }


    @Transactional
    public CreateBudgetResponse createBudget(CreateBudgetRequest req, Long actorUserId) {
        // 1. Basic validation
        if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.AMOUNT_NOT_NEGATIVE);
        }
        if (req.getStartDate() == null || req.getEndDate() == null || req.getEndDate().isBefore(req.getStartDate())) {
            throw new AppException(ErrorCode.INVALID_PERIOD);
        }


        Budget budget = Budget.builder()
                .name(req.getName())
                .ownerId(actorUserId)
                .amount(req.getAmount())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .periodType(req.getPeriodType())
                .status(req.getBudgetStatus())
                .build();

        budgetRepository.save(budget);

        if (req.getScopes() != null) {
            List<BudgetScope> scopes = req.getScopes().stream()
                    .map(s -> BudgetScope.builder()
                            .budget(budget)
                            .scopeType(s.getScopeType())
                            .refId(s.getRefId())
                            .build())
                    .collect(Collectors.toList());
            budgetScopeRepository.saveAll(scopes);
            budget.setScopes(scopes);
        }

        // 5. Persist thresholds
        if (req.getThresholds() != null) {
            List<BudgetThreshold> thresholds = req.getThresholds().stream()
                    .map(t -> BudgetThreshold.builder()
                            .budget(budget)
                            .percent(t.getPercent())
                            .action(t.getAction())
                            .build())
                    .collect(Collectors.toList());
            budgetThresholdRepository.saveAll(thresholds);
            budget.setThresholds(thresholds);
        }

        // 6. Initialize BudgetUsage for the period (upsert style)
        LocalDate periodStart = req.getStartDate();
        LocalDate periodEnd = req.getEndDate();
        BudgetUsage usage = budgetUsageRepository.findByBudgetAndPeriodStartAndPeriodEnd(budget, periodStart, periodEnd)
                .orElseGet(() -> BudgetUsage.builder()
                        .budget(budget)
                        .periodStart(periodStart)
                        .periodEnd(periodEnd)
                        .spentAmount(BigDecimal.ZERO)
                        .build());
        usage = budgetUsageRepository.save(usage);

        // 7. Create a simple history record (summary)
        String note = String.format("Created budget amount=%s period=%s->%s", req.getAmount(), periodStart, periodEnd);
        BudgetHistory history = BudgetHistory.builder()
                .budget(budget)
                .changedBy(actorUserId)
                .changeType(BudgetChangeType.CREATE)
                .note(note)
                .build();
        budgetHistoryRepository.save(history);

        return new CreateBudgetResponse(budget.getId(), budget.getStatus());
    }

    public BudgetResponse updateBudget(Long userId, Long id, BudgetRequest request) {

        return null;
    }

    public void deleteBudget(Long userId, Long id) {

    }
    public Page<BudgetDto> getBudgetsForCurrentUser(Pageable pageable,Long userId) {

        return getBudgetsForUser(userId, pageable);
    }

    @Override
    public Page<BudgetDto> getBudgetsForUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        Page<Budget> page = budgetRepository.findByOwnerId(userId, pageable);
        return page.map(budgetMapper::toDto);
    }

    @Override
    public BudgetDto getBudgetByIdForUser(Long budgetId, Long userId) {
        Budget b = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return budgetMapper.toDto(b);
    }


}