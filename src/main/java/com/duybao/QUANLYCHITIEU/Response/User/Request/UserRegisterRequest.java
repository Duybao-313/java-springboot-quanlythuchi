package com.duybao.QUANLYCHITIEU.Response.User.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @NotBlank(message = "Tài khoản Không được để trống")
    private String username;
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    @NotBlank(message = "Email không được để trống")
    private  String email;
}
