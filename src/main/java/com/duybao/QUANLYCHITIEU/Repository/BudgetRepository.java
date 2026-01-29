package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Enum.BudgetStatus;
import com.duybao.QUANLYCHITIEU.Model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BudgetRepository extends JpaRepository<Budget, Long> {
//    List<Budget> findByOwnerIdAndStatus(Long ownerId, BudgetStatus status);
    Page<Budget> findByOwnerId(Long ownerId, Pageable pageable);
    Page<Budget> findByOwnerIdAndStatus(Long ownerId, BudgetStatus status, Pageable pageable);

//    Page<Budget> findByUserId(Long userId, Pageable pageable);
}

