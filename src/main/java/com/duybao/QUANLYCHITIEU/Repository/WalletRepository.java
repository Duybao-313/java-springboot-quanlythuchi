package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByUserId(Long userId);
    @Query("select Coalesce(sum(t.balance),0) from Wallet t where t.user.id=:userId")
    BigDecimal SumBalanceByUserId(@Param( "userId")Long userId);
}