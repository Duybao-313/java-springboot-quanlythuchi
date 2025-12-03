package com.duybao.QUANLYCHITIEU.Enum;

public enum UserStatus {
    ACTIVE,         // Đang hoạt động
    INACTIVE,       // Không hoạt động (bị khóa hoặc tự khóa)
    PENDING,        // Chờ xác minh (email, số điện thoại...)
    SUSPENDED,      // Bị tạm khóa do vi phạm
    DELETED         // Đã xóa (logic delete)
}