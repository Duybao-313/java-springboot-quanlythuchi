package com.duybao.QUANLYCHITIEU.DTO.request;

import com.duybao.QUANLYCHITIEU.Validator.PassWordMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "PASSWORD_NOT_NULL")
    private String oldPass;
    @PassWordMin(min = 6)
    @NotBlank(message = "PASSWORD_NOT_NULL")
    private String newPass1;
    @NotBlank(message = "PASSWORD_NOT_NULL")
    @PassWordMin(min = 6)
    private String newPass2;
}
