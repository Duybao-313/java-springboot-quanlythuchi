package com.duybao.QUANLYCHITIEU.Exception;

public enum ErrorCode {

    USER_NOT_FOUND("Người dùng không tồn tại"),
    ROLE_NOT_FOUND("ROLE không tồn tại"),
    USERNAME_ALREADY_EXISTS("Username đã tồn tại"),
    EMAIL_ALREADY_EXISTS("Email đã được sử dụng"),
    WALLET_NOT_FOUND("Không tìm thấy ví"),
    CATEGORY_NOT_FOUND("Không tìm thấy danh mục"),
    WALLET_NOT_ENOUGH("Số dư không đủ"),
    USER_NOT_AUTHORIZED("không có quyền"),
    BUDGET_NOT_FOUND("Không tìm thấy budget"),
    TRANSACTION_NOT_FOUND("Không tìm thấy giao dịch"),
    PASSWORD_INCORECT("Mật khẩu không đúng"),
    TOKEN_EXPIRED("token hết hạn"),
    TOKEN_INVALID("token không hợp lệ"),
    INTERNAL_ERROR("lỗi jwt"),
    CATEGORY_EXIST("Danh mục đã tồn tại"),CATEGORY_ALREADY_ASSIGNED("Đã gán danh mục này"),
    AMOUNT_NOT_NEGATIVE("Số tiền không phải số âm"),


    INVALID_REQUEST("Yêu cầu không hợp lệ");




    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}