package com.duybao.QUANLYCHITIEU.Response.User;

import com.duybao.QUANLYCHITIEU.Enum.UserStatus;
import com.duybao.QUANLYCHITIEU.Response.Transaction.TransactionResponse;
import com.duybao.QUANLYCHITIEU.Response.Wallet.WalletResponse;
import com.duybao.QUANLYCHITIEU.Response.category.CategoryResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


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

        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        private LocalDateTime updatedAt;

//        private List<WalletResponse> wallets;
//        private List<TransactionResponse> transactions;
//        private List<CategoryResponse> categories;

        private UserStatus status;
    }
