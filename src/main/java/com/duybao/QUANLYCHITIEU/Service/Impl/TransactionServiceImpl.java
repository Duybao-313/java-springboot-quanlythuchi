package com.duybao.QUANLYCHITIEU.Service.Impl;

import com.duybao.QUANLYCHITIEU.DTO.request.TransferRequest;
import com.duybao.QUANLYCHITIEU.Enum.BudgetAction;
import com.duybao.QUANLYCHITIEU.Enum.BudgetStatus;
import com.duybao.QUANLYCHITIEU.Enum.ScopeType;
import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.TransactionMapper;
import com.duybao.QUANLYCHITIEU.Model.*;
import com.duybao.QUANLYCHITIEU.Repository.*;
import com.duybao.QUANLYCHITIEU.DTO.request.TransactionRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.Transaction.TransactionResponse;
import com.duybao.QUANLYCHITIEU.Service.NotificationService;
import com.duybao.QUANLYCHITIEU.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;
    private final BudgetUsageRepository budgetUsageRepository;
    private final BudgetRepository budgetRepository;
    private final BudgetTransactionRepository budgetTransactionRepository;
    private final BudgetThresholdRepository budgetThresholdRepository;
    private final NotificationService notificationService;

    @Transactional
    public TransactionResponse createTransaction(Long userId, TransactionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        if (!wallet.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new AppException(ErrorCode.AMOUNT_NOT_NEGATIVE);
        }
        BigDecimal amount = request.getAmount();

        // update wallet balance and persist
        if (category.getType() == TransactionType.EXPENSE) {
            wallet.setBalance(wallet.getBalance().subtract(amount));
        } else {
            wallet.setBalance(wallet.getBalance().add(amount));
        }
        walletRepository.save(wallet);

        // persist transaction
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(category.getType())
                .description(request.getDescription())
                .wallet(wallet)
                .category(category)
                .user(user)
                .date(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);
if(transaction.getType()==TransactionType.EXPENSE) {
    List<Budget> budgets = budgetRepository.findApplicableBudgetsForTransaction(
            wallet.getId(),
            category.getId(),
            userId,
            BudgetStatus.ACTIVE,
            ScopeType.CATEGORY,
            ScopeType.WALLET,
            ScopeType.ACCOUNT
    );

    for (Budget b : budgets) {
        LocalDate txDate = transaction.getDate().toLocalDate();
        LocalDate periodStart = computePeriodStart(b, txDate);
        LocalDate periodEnd = computePeriodEnd(b, txDate);

        BudgetUsage usage = budgetUsageRepository
                .findByBudgetIdAndPeriodStartAndPeriodEnd(b.getId(), periodStart, periodEnd)
                .orElseGet(() -> budgetUsageRepository.save(BudgetUsage.builder()
                        .budget(b)
                        .periodStart(periodStart)
                        .periodEnd(periodEnd)
                        .spentAmount(BigDecimal.ZERO)
                        .lastUpdated(LocalDateTime.now())
                        .build()));

        // prevent double-count mapping
        if (budgetTransactionRepository.existsByBudgetIdAndTransactionId(b.getId(), transaction.getId())) {
            continue;
        }

        // increment spent (atomic)
        int updated = budgetUsageRepository.incrementSpent(usage.getId(), amount);
        if (updated == 0) {
            throw new AppException(ErrorCode.BUDGET_UPDATE_FAILED);
        }

        // save mapping with amount and timestamp
        BudgetTransaction bt = BudgetTransaction.builder()
                .budget(b)
                .transaction(transaction)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();
        budgetTransactionRepository.save(bt);

        // reload spent and check thresholds
        BigDecimal spent = budgetUsageRepository.findById(usage.getId())
                .map(BudgetUsage::getSpentAmount)
                .orElse(BigDecimal.ZERO);
        log.info(spent.toString());
        checkAndTriggerThresholds(b, amount, usage, transaction);

    }
}
        return transactionMapper.toDTO(transaction);
    }

    public TransactionResponse transferTransaction(Long userId, TransferRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Wallet walletTF = walletRepository.findById(request.getWalletIdTransfer())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        Wallet walletRE = walletRepository.findById(request.getWalletIdReceive())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        BigDecimal balanceTF  =walletTF.getBalance();
        BigDecimal amount  =request.getAmount();
        BigDecimal balanceRE  =walletRE.getBalance();
        walletTF.setBalance(balanceTF.subtract(amount));
        walletRE.setBalance(balanceRE.add(amount));
        walletRepository.save(walletRE);
        Category category= categoryRepository.findByNameIgnoreCase("Chuyển tiền").orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        Category categoryRE= categoryRepository.findByNameIgnoreCase("Nhận tiền").orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(category.getType())
                .description(request.getDescription())
                .category(category)
                .wallet(walletTF )
                .user(user)
                .date(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);


        TransactionRequest transaction1= TransactionRequest.builder()
                .amount(amount)
                .walletId(request.getWalletIdReceive())
                .categoryId(categoryRE.getId())
                .description(request.getDescription())
                .build();
         TransactionResponse abc = createTransaction(userId, transaction1);

        return transactionMapper.toDTO(transaction);

    };

    public List<TransactionResponse> getTransactionsByUser(   Long userId,
                                                              TransactionType type,
                                                              LocalDate startDate,
                                                              LocalDate endDate,
                                                              Long categoryId,
                                                              Long walletId) {

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        var transactions = transactionRepository.findByUserAndOptionalFilters(
                userId,type, categoryId, walletId, start, end);

        return transactionMapper.toDTOs(transactions);
    }

    public TransactionResponse updateTransaction(Long userId, Long id, TransactionRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
//        transaction.setType(request.getType());
        transaction.setDate(LocalDateTime.now());

        Transaction updated = transactionRepository.save(transaction);
        return transactionMapper.toDTO(updated);

    }

    public void deleteTransaction(Long userId, Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        transactionRepository.delete(transaction);
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
        };}
    private void checkAndTriggerThresholds(Budget budget, BigDecimal spent, BudgetUsage usage, Transaction tx) {
        BigDecimal budgetAmount = budget.getAmount() == null ? BigDecimal.ZERO : budget.getAmount();
        if (budgetAmount.compareTo(BigDecimal.ZERO) == 0) return;

        int percentUsed = spent.multiply(BigDecimal.valueOf(100))
                .divide(budgetAmount, 0, RoundingMode.DOWN).intValue();

        List<BudgetThreshold> thresholds = budgetThresholdRepository.findByBudgetIdOrderByPercentAsc(budget.getId());

        for (BudgetThreshold t : thresholds) {
            if (Boolean.TRUE.equals(t.getTriggered())) continue;

            if (percentUsed >= t.getPercent()) {
                t.setTriggered(true);
                t.setTriggeredAt(LocalDateTime.now());
                log.info("1223311{}", t.getAction());
                budgetThresholdRepository.save(t);

                // gọi notification via abstraction
                log.info("1223311{}", t.getAction());
                if (t.getAction()== BudgetAction.NOTIFY) {
                    notificationService.notifyBudgetThresholdReached(
                            budget.getOwnerId(),
                            budget.getId(),
                            t.getPercent(),
                            spent,
                            budgetAmount,
                            tx.getId()
                    );
                } else if (t.getAction()== BudgetAction.BLOCK) {
                    notificationService.notifyBudgetBlocked(
                            budget.getOwnerId(),
                            budget.getId(),
                            t.getPercent(),
                            spent,
                            budgetAmount,
                            tx.getId()
                    );
                }
            }
        }
    }

}


