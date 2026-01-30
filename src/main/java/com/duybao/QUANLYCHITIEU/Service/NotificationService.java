package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.DTO.Response.Notic.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface NotificationService {
    void notifyBudgetThresholdReached(Long userId, Long budgetId, int percent, BigDecimal spent, BigDecimal budgetAmount, Long transactionId);
    void notifyBudgetBlocked(Long userId, Long budgetId, int percent, BigDecimal spent, BigDecimal budgetAmount, Long transactionId);
     Page<NotificationDto> listNotifications(Long userId, boolean unreadOnly, Pageable pageable);
    NotificationDto getDetail(Long userId, Long id);
    void markReadBulk(Long userId, List<Long> ids);
    void markRead(Long userId, Long id);
    long countUnread(Long userId);
}