package com.duybao.QUANLYCHITIEU.Service.Impl;

import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.WalletMapper;
import com.duybao.QUANLYCHITIEU.Model.Transaction;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Model.Wallet;
import com.duybao.QUANLYCHITIEU.Repository.TransactionRepository;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;
import com.duybao.QUANLYCHITIEU.Repository.WalletRepository;
import com.duybao.QUANLYCHITIEU.Response.Wallet.Request.WalletRequest;
import com.duybao.QUANLYCHITIEU.Response.Wallet.WalletResponse;
import com.duybao.QUANLYCHITIEU.Service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final WalletMapper walletMapper;
    private  final TransactionRepository transactionRepository;

    public WalletResponse createWallet(Long userId, WalletRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Wallet wallet = Wallet.builder()
                .name(request.getName())
                .balance(request.getBalance())
                .type(request.getType())
                .iconUrl(request.getIconUrl())
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
            throw new AppException(ErrorCode.USER_NOT_AUTHORIZED);
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
            throw new AppException(ErrorCode.USER_NOT_AUTHORIZED);
        }
        if(!transactionRepository.findByUserAndWallet(userId,id).isEmpty()){
            List<Transaction> txs = transactionRepository.findByUserAndWallet(userId, id);
            transactionRepository.deleteAll(txs);
        }
        walletRepository.delete(wallet);
    }

}
