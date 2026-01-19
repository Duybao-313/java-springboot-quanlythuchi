package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.DTO.request.TransferRequest;
import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import com.duybao.QUANLYCHITIEU.DTO.request.TransactionRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.Transaction.TransactionResponse;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

        public TransactionResponse createTransaction(Long userId, TransactionRequest request);
    public TransactionResponse transferTransaction(Long userId, TransferRequest request);

    public List<TransactionResponse> getTransactionsByUser(Long userId, TransactionType type,
                                                           LocalDate startDate,
                                                           LocalDate endDate,
                                                           Long categoryId,
                                                           Long walletId)   ;
    public TransactionResponse updateTransaction(Long userId, Long id, TransactionRequest request);


    public void deleteTransaction(Long userId, Long id) ;
    }

