package com.duybao.QUANLYCHITIEU.DTO.Response.Admin;

import com.duybao.QUANLYCHITIEU.Enum.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserAdminDto {
    private String fullName;
    private String email;
    private UserStatus status;
    private LocalDateTime createdAt;
    private  Long walletCount;
}