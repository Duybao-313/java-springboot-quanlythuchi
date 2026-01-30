package com.duybao.QUANLYCHITIEU.DTO.Response.Notic;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private String title;
    private String body;
    private Boolean readFlag;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}