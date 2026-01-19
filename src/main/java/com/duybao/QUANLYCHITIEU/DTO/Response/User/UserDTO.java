package com.duybao.QUANLYCHITIEU.DTO.Response.User;

import com.duybao.QUANLYCHITIEU.Enum.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class UserDTO {
        private Long id;
        private String username;
        private String email;
        private String fullName;
        private String phone;
        private String avatarUrl;
        private String address;
        private String role;

        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        private LocalDateTime updatedAt;

//        private List<WalletResponse> wallets;
//        private List<TransactionResponse> transactions;
//        private List<CategoryResponse> categories;

        private UserStatus status;
    }
