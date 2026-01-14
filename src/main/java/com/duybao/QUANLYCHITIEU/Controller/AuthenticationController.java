package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.DTO.request.LogoutRequest;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.DTO.request.UserLoginRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.UserRegisterRequest;
import com.duybao.QUANLYCHITIEU.Mappers.UserMapper;
import com.duybao.QUANLYCHITIEU.DTO.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.AuthResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.RegisterResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.Service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> registeracc(@Valid @RequestBody UserRegisterRequest a){

        return ApiResponse.<RegisterResponse>builder()
                .success(true)
                .message("Đăng ký thành công")
                .code(200)
                .data(authenticationService.UserRegister(a))
                .build();


    }
    @PostMapping ("/login")
    public ApiResponse<AuthResponse>userinfo(@Valid @RequestBody UserLoginRequest a){
                return ApiResponse.<AuthResponse>builder()
                        .success(true)
                .code(200)
                .message("Đăng nhập thành công")
                .data(authenticationService.login(a))
                .build();

    }

    @PostMapping("/code") String testjwt (@RequestBody UserLoginRequest a){
        return authenticationService.jwtcode(a);
    }
    @GetMapping("/userdetail")
    public ApiResponse<UserDTO> getCurrentUser(@AuthenticationPrincipal User customUserDetail) {

        return ApiResponse.<UserDTO>builder()
                .data(authenticationService.getUser(customUserDetail.getId()))
                .success(true)
                .message("Lấy thông tin người dùng")

                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostMapping ("/logout") ApiResponse<String> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
       authenticationService.Logout(request);
        return ApiResponse.<String>builder()
                .success(true)
                .message("Đăng xuất thành công")
                .build();
    }


}