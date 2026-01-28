package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Enum.BudgetStatus;
import com.duybao.QUANLYCHITIEU.Model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByOwnerIdAndStatus(Long ownerId, BudgetStatus status);
}

