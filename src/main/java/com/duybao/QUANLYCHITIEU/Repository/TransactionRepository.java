package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.wallet.id = :walletId")
    List<Transaction> findByUserAndWallet(@Param("userId") Long userId, @Param("walletId") Long walletId);
}