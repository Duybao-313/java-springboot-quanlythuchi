package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Model.Transaction;
import com.duybao.QUANLYCHITIEU.Response.Transaction.Request.TransactionRequest;
import com.duybao.QUANLYCHITIEU.Response.Transaction.TransactionResponse;

import java.util.List;

public interface TransactionService {

        public TransactionResponse createTransaction(Long userId, TransactionRequest request);

        public List<TransactionResponse> getTransactionsByUser(Long userId);
    public TransactionResponse updateTransaction(Long userId, Long id, TransactionRequest request);

    public void deleteTransaction(Long userId, Long id) ;
    }

