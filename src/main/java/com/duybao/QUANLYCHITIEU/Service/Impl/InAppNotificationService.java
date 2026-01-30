package com.duybao.QUANLYCHITIEU.Service.Impl;

import com.duybao.QUANLYCHITIEU.DTO.Response.Notic.NotificationDto;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Model.BudgetThreshold;
import com.duybao.QUANLYCHITIEU.Model.Notification;
import com.duybao.QUANLYCHITIEU.Repository.BudgetRepository;
import com.duybao.QUANLYCHITIEU.Repository.NotificationRepository;
import com.duybao.QUANLYCHITIEU.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InAppNotificationService implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final BudgetRepository budgetRepository;


        @Override
        public void notifyBudgetThresholdReached(Long userId, Long budgetId, int percent, BigDecimal spent, BigDecimal budgetAmount, Long transactionId) {
            String name=budgetRepository.findById(budgetId).orElseThrow(()-> new AppException(ErrorCode.BUDGET_NOT_FOUND)).getName();
            Notification n = Notification.builder()
                    .userId(userId)
                    .title("Ngân sách đạt ngưỡng " + percent + "%")
                    .body(String.format("Ngân sách %s đã đạt %d%% (%s/%s). Giao dịch %d", name, percent, spent, budgetAmount, transactionId))
                    .readFlag(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(n);
        }

        @Override
        public void notifyBudgetBlocked(Long userId, Long budgetId, int percent, BigDecimal spent, BigDecimal budgetAmount, Long transactionId) {
            String name=budgetRepository.findById(budgetId).orElseThrow(()-> new AppException(ErrorCode.BUDGET_NOT_FOUND)).getName();

            Notification n = Notification.builder()
                    .userId(userId)
                    .title("Giao dịch đã vượt mức")
                    .body(String.format("Giao dịch %d đã vượt ngưỡng %d%% của ngân sách %s.", transactionId, percent, name))
                    .readFlag(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(n);
        }
    @Transactional(readOnly = true)
    public Page<NotificationDto> listNotifications(Long userId, boolean unreadOnly, Pageable pageable) {
        Page<Notification> page;
        if (unreadOnly) {
            page = notificationRepository.findAll(
                    (root, query, cb) -> cb.and(
                            cb.equal(root.get("userId"), userId),
                            cb.isFalse(root.get("readFlag"))
                    ), pageable);
        } else {
            page = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        }
        return page.map(this::toDto);
    }

    @Transactional(readOnly = true)
    public long countUnread(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Transactional
    public void markRead(Long userId, Long id) {
        int updated = notificationRepository.markReadIfOwner(id, userId, LocalDateTime.now());
        if (updated == 0) {
            throw new AppException(ErrorCode.NOTIFICATION_NOT_FOUND_OR_FORBIDDEN);
        }
    }

    @Transactional
    public void markReadBulk(Long userId, List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        notificationRepository.markReadBulk(ids, userId, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public NotificationDto getDetail(Long userId, Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND_OR_FORBIDDEN));
        if (!Objects.equals(n.getUserId(), userId)) {
            throw new AppException(ErrorCode.NOTIFICATION_NOT_FOUND_OR_FORBIDDEN);
        }
        return toDto(n);
    }

    private NotificationDto toDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId())
                .title(n.getTitle())
                .body(n.getBody())
                .readFlag(n.getReadFlag())
                .createdAt(n.getCreatedAt())
                .readAt(n.getReadAt())
                .build();
    }

}