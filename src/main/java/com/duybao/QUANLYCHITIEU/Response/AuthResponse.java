package com.duybao.QUANLYCHITIEU.Response;

import com.duybao.QUANLYCHITIEU.Response.User.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    String jwt;
    String role;
    UserDTO a;
}
