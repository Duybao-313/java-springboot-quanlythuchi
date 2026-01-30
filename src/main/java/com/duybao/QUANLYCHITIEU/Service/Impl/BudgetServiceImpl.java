package com.duybao.QUANLYCHITIEU.Service.Impl;

import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetDetailResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.CreateBudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.*;
import com.duybao.QUANLYCHITIEU.Enum.BudgetChangeType;
import com.duybao.QUANLYCHITIEU.Enum.BudgetStatus;
import com.duybao.QUANLYCHITIEU.Enum.ScopeType;
import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.BudgetMapper;
import com.duybao.QUANLYCHITIEU.Model.*;
import com.duybao.QUANLYCHITIEU.Repository.*;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetResponse;
import com.duybao.QUANLYCHITIEU.Service.BudgetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private final BudgetTransactionRepository budgetTransactionRepository;
    private final ObjectMapper objectMapper; // for optional serialization
    private final UserRepository userRepository;





    @Transactional
    public CreateBudgetResponse createBudget(CreateBudgetRequest req, Long actorUserId) {
        // 1. Basic validation
        if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.AMOUNT_NOT_NEGATIVE);
        }
        if (req.getStartDate() == null || req.getEndDate() == null || req.getEndDate().isBefore(req.getStartDate())) {
            throw new AppException(ErrorCode.INVALID_PERIOD);
        }
        if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.AMOUNT_NOT_NEGATIVE);
        }
        if (req.getStartDate() == null || req.getEndDate() == null || req.getEndDate().isBefore(req.getStartDate())) {
            throw new AppException(ErrorCode.INVALID_PERIOD);
        }

        // --- NEW: validate scopes before saving budget ---
        if (req.getScopes() != null) {
            // Option A: check one-by-one (simple)
            for (ScopeDto s : req.getScopes()) {
                if (s.getScopeType() == ScopeType.CATEGORY) {
                    Category cat = categoryRepository.findById(s.getRefId())
                            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
                    if (cat.getType() != TransactionType.EXPENSE) {
                        throw new AppException(ErrorCode.INVALID_REQUEST);
                    }
                }
            }}


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
@Transactional
    public void updateBudget(User user, Long id, UpdateBudgetRequest request) {
        Budget budget=budgetRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.BUDGET_NOT_FOUND));
        if(!Objects.equals(user.getRole().getName(), "ROLE_ADMIN") && !Objects.equals(user.getId(), budget.getOwnerId()))
        {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
//        VALIDATE
    if(request.getStartDate().isAfter(request.getEndDate()))
    {  throw new AppException(ErrorCode.INVALID_REQUEST);}
//
        budget.setAmount(request.getAmount());
        budget.setName(request.getName());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());
        budget.setUpdatedAt(LocalDateTime.now());
        budget.setPeriodType(request.getPeriodType());
        budget.setStatus(request.getBudgetStatus());
        if (request.getScopes() != null) {
            budgetScopeRepository.deleteByBudgetId(id);
            List<BudgetScope> scopes = request.getScopes().stream()
                    .map(s -> BudgetScope.builder()
                            .budget(budget)
                            .scopeType(s.getScopeType())
                            .refId(s.getRefId())
                            .build())
                    .toList();
            if (budget.getScopes() == null) {
                budget.setScopes(new ArrayList<>());
            } else {
                budget.getScopes().clear(); // Hibernate will mark orphans for deletion
            }
            budget.getScopes().addAll(scopes);
        }

        if (request.getThresholds() != null) {
            budgetThresholdRepository.deleteByBudgetId(id);
            List<BudgetThreshold> thresholds = request.getThresholds().stream()
                    .map(t -> BudgetThreshold.builder()
                            .budget(budget)
                            .percent(t.getPercent())
                            .action(t.getAction())
                            .build())
                    .toList();
            if (budget.getThresholds() == null) {
                budget.setThresholds(new ArrayList<>());
            } else {
                budget.getThresholds().clear();
            }
            budget.getThresholds().addAll(thresholds);
        }


        budgetRepository.save(budget);

        LocalDate periodStart = request.getStartDate();
        LocalDate periodEnd = request.getEndDate();
        BudgetUsage usage = budgetUsageRepository.findByBudgetAndPeriodStartAndPeriodEnd(budget, periodStart, periodEnd)
                .orElseGet(() -> BudgetUsage.builder()
                        .budget(budget)
                        .periodStart(periodStart)
                        .periodEnd(periodEnd)
                        .spentAmount(BigDecimal.ZERO)
                        .build());
        usage = budgetUsageRepository.save(usage);

        String note = String.format("update budget amount=%s period=%s->%s", request.getAmount(), periodStart, periodEnd);
        BudgetHistory history = BudgetHistory.builder()
                .budget(budget)
                .changedBy(user.getId())
                .changeType(BudgetChangeType.CREATE)
                .note(note)
                .build();
        budgetHistoryRepository.save(history);
    }
@Transactional
    public void deleteBudget(Long userId, Long budget_id) {
        Budget b=budgetRepository.findById(budget_id).orElseThrow(()->new AppException(ErrorCode.BUDGET_NOT_FOUND));
       User u=userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
        if(!Objects.equals(u.getRole().getName(), "ROLE_ADMIN"))
        {if(!b.getOwnerId().equals(userId))
        { throw new AppException(ErrorCode.UNAUTHENTICATED);}}
        budgetTransactionRepository.deleteByBudgetId(b.getId());
        budgetRepository.delete(b);
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
    public BudgetDetailResponse getBudgetByIdForUser(Long budgetId, Long userId) {
        Budget b = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new AppException(ErrorCode.BUDGET_NOT_FOUND));
           User u=userRepository.findById(userId).orElseThrow();
            if(!Objects.equals(u.getRole().getName(), "ROLE_ADMIN"))
             {if(!b.getOwnerId().equals(userId))
                 { throw new AppException(ErrorCode.UNAUTHENTICATED);}}

        BigDecimal spent = getSpentForBudget(b, LocalDate.now());

        BigDecimal amount = b.getAmount() == null ? BigDecimal.ZERO : b.getAmount();
        BigDecimal remaining = amount.subtract(spent).max(BigDecimal.ZERO);
        int percentUsed = amount.compareTo(BigDecimal.ZERO) > 0
                ? spent.multiply(BigDecimal.valueOf(100)).divide(amount, 0, RoundingMode.DOWN).intValue()
                : 0;
        return BudgetDetailResponse.builder()
                .name(b.getName())
                .budgetStatus(b.getStatus())
                .thresholds(b.getThresholds().stream().map(budgetMapper::toThresholdDto).toList())
                .scopes(b.getScopes().stream().map(budgetMapper::toScopeDto).toList())
                .endDate(b.getEndDate())
                .startDate(b.getStartDate())
                .amount(b.getAmount())
                .spent(spent)
                .remaining(remaining)
                .percentUsed(percentUsed)
                .periodType(b.getPeriodType())
                .build();
    }



    public BigDecimal getSpentForBudget(Budget budget, LocalDate now) {
        LocalDate periodStart = computePeriodStart(budget, now);
        LocalDate periodEnd = computePeriodEnd(budget, now);

        return budgetUsageRepository.findByBudgetAndPeriodStartAndPeriodEnd(budget, periodStart, periodEnd)
                .map(BudgetUsage::getSpentAmount)
                .orElse(BigDecimal.ZERO);
    }
    private LocalDate computePeriodStart(Budget b, LocalDate now) {
        return switch (b.getPeriodType()) {
            case MONTHLY -> now.withDayOfMonth(1);
            case WEEKLY -> now.with(ChronoField.DAY_OF_WEEK, 1); // hoặc WeekFields
            case ONE_TIME -> b.getStartDate();
            default -> b.getStartDate();
        };
    }
    private LocalDate computePeriodEnd(Budget b, LocalDate now) {
        return switch (b.getPeriodType()) {
            case MONTHLY -> now.withDayOfMonth(now.lengthOfMonth());
            case WEEKLY -> computePeriodStart(b, now).plusDays(6);
            case ONE_TIME -> b.getEndDate();
            default -> b.getEndDate();
        };
    }
}