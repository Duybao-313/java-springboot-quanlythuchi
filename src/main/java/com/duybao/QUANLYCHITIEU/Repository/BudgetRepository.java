package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Enum.BudgetStatus;
import com.duybao.QUANLYCHITIEU.Enum.ScopeType;
import com.duybao.QUANLYCHITIEU.Model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface BudgetRepository extends JpaRepository<Budget, Long> {
//    List<Budget> findByOwnerIdAndStatus(Long ownerId, BudgetStatus status);
    Page<Budget> findByOwnerId(Long ownerId, Pageable pageable);
    Page<Budget> findByOwnerIdAndStatus(Long ownerId, BudgetStatus status, Pageable pageable);
    @Query("""
    select distinct b from Budget b
    join b.scopes s
    where b.status = :status and (
      (s.scopeType = :categoryScope and s.refId = :categoryId) or
      (s.scopeType = :walletScope and s.refId = :walletId) or
      (s.scopeType = :userScope and b.ownerId = :userId)
    )
    """)
    List<Budget> findApplicableBudgetsForTransaction(
            @Param("walletId") Long walletId,
            @Param("categoryId") Long categoryId,
            @Param("userId") Long userId,
            @Param("status") BudgetStatus status,
            @Param("categoryScope") ScopeType categoryScope,
            @Param("walletScope") ScopeType walletScope,
            @Param("userScope") ScopeType userScope);



}

