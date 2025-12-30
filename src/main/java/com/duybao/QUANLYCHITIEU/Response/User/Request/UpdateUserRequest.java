package com.duybao.QUANLYCHITIEU.Response.User.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {
    private String fullName;
    private String email;
    private String phone;
    private String address;
}