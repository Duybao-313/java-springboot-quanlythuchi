package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.DTO.Response.TokenResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.LogoutRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.UserLoginRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.UserRegisterRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.AuthResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.RegisterResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.User.UserDTO;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;


public interface AuthenticationService {
    public RegisterResponse UserRegister(UserRegisterRequest a);

   public AuthResponse login(UserLoginRequest a);


    public UserDTO getUser(Long id);
    public void Logout(LogoutRequest request) throws ParseException, JOSEException;
}
