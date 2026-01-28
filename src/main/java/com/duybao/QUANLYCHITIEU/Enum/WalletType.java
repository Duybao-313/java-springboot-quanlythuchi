package com.duybao.QUANLYCHITIEU.Enum;

import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum WalletType {
    CASH,        // Tiền mặt
    BANK,        // Tài khoản ngân hàng
    E_WALLET ;    // Ví điện t
    @JsonCreator
    public static WalletType from(String value) {
        if (value == null) {
            throw new AppException(ErrorCode.TYPE_NOT_NULL);
        }

        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TYPE));
    }
}