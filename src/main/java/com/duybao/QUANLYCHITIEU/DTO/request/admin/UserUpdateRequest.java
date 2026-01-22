package com.duybao.QUANLYCHITIEU.DTO.request.admin;

import com.duybao.QUANLYCHITIEU.Enum.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserUpdateRequest {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private UserStatus status;
    private String role;
}