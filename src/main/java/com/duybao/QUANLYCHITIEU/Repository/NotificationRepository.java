package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.readFlag = false")
    long countUnreadByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.readFlag = true, n.readAt = :time WHERE n.id = :id AND n.userId = :userId AND n.readFlag = false")
    int markReadIfOwner(@Param("id") Long id, @Param("userId") Long userId, @Param("time") LocalDateTime time);

    @Modifying
    @Query("UPDATE Notification n SET n.readFlag = true, n.readAt = :time WHERE n.id IN :ids AND n.userId = :userId AND n.readFlag = false")
    int markReadBulk(@Param("ids") List<Long> ids, @Param("userId") Long userId, @Param("time") LocalDateTime time);
}