package com.duybao.QUANLYCHITIEU.Service.Impl;

import com.duybao.QUANLYCHITIEU.DTO.request.TransferRequest;
import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.TransactionMapper;
import com.duybao.QUANLYCHITIEU.Model.Category;
import com.duybao.QUANLYCHITIEU.Model.Transaction;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Model.Wallet;
import com.duybao.QUANLYCHITIEU.Repository.CategoryRepository;
import com.duybao.QUANLYCHITIEU.Repository.TransactionRepository;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;
import com.duybao.QUANLYCHITIEU.Repository.WalletRepository;
import com.duybao.QUANLYCHITIEU.DTO.request.TransactionRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.Transaction.TransactionResponse;
import com.duybao.QUANLYCHITIEU.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public TransactionResponse createTransaction(Long userId, TransactionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        if (!wallet.getUser().getId().equals(userId)) {
            throw new  AppException(ErrorCode.UNAUTHENTICATED);
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new AppException(ErrorCode.AMOUNT_NOT_NEGATIVE );
        }
        BigDecimal balance  =wallet.getBalance();
        BigDecimal amount  =request.getAmount();


        if (category.getType() == TransactionType.EXPENSE) {

            wallet.setBalance(balance.subtract(amount));
        } else if (category.getType() == TransactionType.INCOME) {
            wallet.setBalance(balance.add(amount));
        }
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(category.getType())
                .description(request.getDescription())
                .wallet(wallet)
                .category(category)
                .user(user)
                .date(LocalDateTime.now())
                .build();

         transactionRepository.save(transaction);
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
}


