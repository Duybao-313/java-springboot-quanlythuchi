package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.DTO.request.UserLoginRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.UserRegisterRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.AuthResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.RegisterResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.User.UserDTO;


public interface AuthenticationService {
    public RegisterResponse UserRegister(UserRegisterRequest a);

   public AuthResponse login(UserLoginRequest a);

   public  String jwtcode(UserLoginRequest a);
    public UserDTO getUser(Long id);
}
