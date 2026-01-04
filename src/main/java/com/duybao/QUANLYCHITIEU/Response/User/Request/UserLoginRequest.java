package com.duybao.QUANLYCHITIEU.Response.User.Request;

import com.duybao.QUANLYCHITIEU.Validator.PassWordMin;
import com.duybao.QUANLYCHITIEU.Validator.UniqueUserName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotBlank(message = "USERNAME_NOT_NULL")
    private String username;
    @NotBlank(message = "PASSWORD_NOT_NULL")
    @PassWordMin(min = 6)
    private String password;
}
