package com.duybao.QUANLYCHITIEU.Service.Impl;

import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.WalletMapper;
import com.duybao.QUANLYCHITIEU.Model.Transaction;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Model.Wallet;
import com.duybao.QUANLYCHITIEU.Repository.TransactionRepository;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;
import com.duybao.QUANLYCHITIEU.Repository.WalletRepository;
import com.duybao.QUANLYCHITIEU.DTO.request.WalletRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.Wallet.WalletOverview;
import com.duybao.QUANLYCHITIEU.DTO.Response.Wallet.WalletResponse;
import com.duybao.QUANLYCHITIEU.Service.ImageService;
import com.duybao.QUANLYCHITIEU.Service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final WalletMapper walletMapper;
    private  final TransactionRepository transactionRepository;
    private final ImageService imageService;

    public WalletResponse createWallet(Long userId, WalletRequest request, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String icon=null;
        if(file!=null){
            try{
                icon=imageService.uploadImage(file,"QLCT-image");
            } catch (Exception ex) {
                throw  new AppException(ErrorCode.READ_FILE_ERROR);
            }
        }

        Wallet wallet = Wallet.builder()
                .name(request.getName())
                .balance(request.getBalance())
                .type(request.getType())
                .iconUrl(icon)
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        walletRepository.save(wallet);

        return walletMapper.toDTO(wallet) ;
    }

    public List<WalletResponse> getWalletsByUser(Long userId) {
        return walletMapper.toDTOs(walletRepository.findByUserId(userId));
    }
    public WalletResponse updateWallet(Long userId, Long id, WalletRequest request) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        if (!wallet.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        wallet.setName(request.getName());
        wallet.setBalance(request.getBalance());
        wallet.setType(request.getType());
        wallet.setIconUrl(request.getIconUrl());

        Wallet updated = walletRepository.save(wallet);
        return walletMapper.toDTO(updated);
    }

    public void deleteWallet(Long userId, Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        if (!wallet.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if(!transactionRepository.findByUserAndWallet(userId,id).isEmpty()){
            List<Transaction> txs = transactionRepository.findByUserAndWallet(userId, id);
            transactionRepository.deleteAll(txs);
        }
        walletRepository.delete(wallet);
    }
    public WalletOverview WalletOverview(Long userId, LocalDateTime startDate, LocalDateTime endDate){
System.out.println("heel"+startDate);
            Long IncomeCount = transactionRepository.findByUserIdAndType(userId, TransactionType.INCOME).stream().count();
            Long ExpenseCount = transactionRepository.findByUserIdAndType(userId, TransactionType.EXPENSE).stream().count();
            BigDecimal totalIncome = transactionRepository.sumAmountByUserIdAndType(userId, TransactionType.INCOME,startDate,endDate);
            BigDecimal totalExpense = transactionRepository.sumAmountByUserIdAndType(userId, TransactionType.EXPENSE,startDate,endDate);
            BigDecimal netBalance = totalIncome.subtract(totalExpense);
            BigDecimal total = walletRepository.SumBalanceByUserId(userId);

        return WalletOverview.builder()
                .walletCount(walletRepository.findAll().size())
                .totalBalance(total)
                .IncomeCount(IncomeCount)
                .ExpenseCount(ExpenseCount)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netBalance(netBalance)
                .build();

    };

}
