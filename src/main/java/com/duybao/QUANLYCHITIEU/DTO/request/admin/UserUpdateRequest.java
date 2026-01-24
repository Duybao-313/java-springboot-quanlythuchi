package com.duybao.QUANLYCHITIEU.DTO.request.admin;

import com.duybao.QUANLYCHITIEU.Enum.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserUpdateRequest {
    private Long id;
    @NotBlank(message = "")
    private String username;
    @NotBlank(message = "")
    private String fullName;
    @Email(message = "EMAIL_INVALID")
    private String email;
    private UserStatus status;
    private String role;
}