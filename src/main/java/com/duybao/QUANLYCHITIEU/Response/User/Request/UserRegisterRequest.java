package com.duybao.QUANLYCHITIEU.Response.User.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @NotBlank(message = "Không được để trống")
    private String username;
    @NotBlank(message = "Không được để trống")
    private String password;
    @NotBlank(message = "Không được để trống")
    private  String email;
}
