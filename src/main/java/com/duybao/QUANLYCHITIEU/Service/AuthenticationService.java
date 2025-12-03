package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.Response.User.Request.UserLoginRequest;
import com.duybao.QUANLYCHITIEU.Response.User.Request.UserRegisterRequest;
import com.duybao.QUANLYCHITIEU.Response.AuthResponse;
import com.duybao.QUANLYCHITIEU.Response.RegisterResponse;
import com.duybao.QUANLYCHITIEU.Response.User.UserDTO;


public interface AuthenticationService {
    public RegisterResponse UserRegister(UserRegisterRequest a);

   public AuthResponse login(UserLoginRequest a);

   public  String jwtcode(UserLoginRequest a);
    public UserDTO getUser(Long id);
}
