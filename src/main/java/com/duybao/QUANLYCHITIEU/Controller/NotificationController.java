package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.DTO.Response.Notic.NotificationDto;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public Page<NotificationDto> list(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "false") boolean unreadOnly,
            Pageable pageable) {
        return notificationService.listNotifications(user.getId(), unreadOnly, pageable);
    }

    @GetMapping("/unread-count")
    public Map<String, Long> unreadCount(@AuthenticationPrincipal User user) {
        long count = notificationService.countUnread(user.getId());
        return Collections.singletonMap("unreadCount", count);
    }

    @GetMapping("/{id}")
    public NotificationDto detail(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return notificationService.getDetail(user.getId(), id);
    }

    @PatchMapping("/{id}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markRead(@AuthenticationPrincipal User userId, @PathVariable Long id) {
        notificationService.markRead(userId.getId(), id);
    }

    @PatchMapping("/mark-read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markReadBulk(@AuthenticationPrincipal User user, @RequestBody List<Long> ids) {
        notificationService.markReadBulk(user.getId(), ids);
    }
}