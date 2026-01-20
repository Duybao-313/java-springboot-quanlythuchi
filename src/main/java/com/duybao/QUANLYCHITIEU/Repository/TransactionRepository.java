package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import com.duybao.QUANLYCHITIEU.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> deleteAllByCategoryId(Long categoryId);
    List<Transaction> findByUserIdAndType(Long userId,TransactionType type);
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.wallet.id = :walletId")
    List<Transaction> findByUserAndWallet(@Param("userId") Long userId, @Param("walletId") Long walletId);
    @Query("""
            SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t
             WHERE
                    t.user.id = :userId AND t.type = :type\s
                    and (:start is null or t.date >= :start)
                    and (:end is null or t.date <= :end)
                  \s""")
    BigDecimal sumAmountByUserIdAndType(@Param("userId") Long userId,
                                        @Param("type") TransactionType type,
                                        @Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);


    @Query("""
        select t
        from Transaction t
        left join fetch t.category c
        left join fetch t.user u
        left join fetch t.wallet w
        where u.id = :userId
          and (:categoryId is null or c.id = :categoryId)
          and (:type is null or t.type = :type)
          and (:walletId is null or w.id = :walletId)
          and (:start is null or t.date >= :start)
          and (:end is null or t.date <= :end)
        order by t.date desc
        """)
    List<Transaction> findByUserAndOptionalFilters(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("categoryId") Long categoryId,
            @Param("walletId") Long walletId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
    @Query("Select Count(t) from Transaction t where t.date between :start and :end")
    Long countTransactionByDate(LocalDateTime start,LocalDateTime end);
}