package com.duybao.QUANLYCHITIEU.DTO.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {
    private String fullName;
    @Email(message = "EMAIL_INVALID")
    private  String email;
    private String phone;
    private String address;
}