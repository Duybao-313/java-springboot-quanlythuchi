package com.duybao.QUANLYCHITIEU.Service.Impl;


import com.duybao.QUANLYCHITIEU.Response.User.Request.UserLoginRequest;
import com.duybao.QUANLYCHITIEU.Response.User.Request.UserRegisterRequest;
import com.duybao.QUANLYCHITIEU.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.Enum.UserStatus;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.UserMapper;
import com.duybao.QUANLYCHITIEU.Model.CustomUserDetail;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.RoleRepository;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;

import com.duybao.QUANLYCHITIEU.Response.AuthResponse;
import com.duybao.QUANLYCHITIEU.Response.RegisterResponse;
import com.duybao.QUANLYCHITIEU.Service.AuthenticationService;
import com.duybao.QUANLYCHITIEU.Service.JwtService;



import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public RegisterResponse UserRegister(UserRegisterRequest a) {
        if (userRepository.findByUsername(a.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.findByEmail(a.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User user = new User();
        user = userMapper.toEntity(a);
        user.setPassword(passwordEncoder.encode(a.getPassword()));
        user.setRole(roleRepository.findById(1).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
        // Lưu user
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        return new RegisterResponse().builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build();


    }



    @Override
    public AuthResponse login(UserLoginRequest a) {
        try {
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(a.getUsername(), a.getPassword())
            );

            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            User User = userDetail.getUser();
            String token = jwtService.generateToken(User.getUsername(), User.getRole().getName(), User.getId());
            UserDTO userDTO = userMapper.toDTO(User); //
            String role = User.getRole().getName();
            return new AuthResponse(token, role, userDTO);
        }
catch (BadCredentialsException e) {
            // Sai mật khẩu
            throw new AppException(ErrorCode.PASSWORD_INCORECT);
        }
    }

    @Override
    public String jwtcode(UserLoginRequest a) {
        try {
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(a.getUsername(), a.getPassword())
            );
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

            String token = jwtService.generateToken(customUserDetail.getUsername(), customUserDetail.getUser().getRole().getName(), customUserDetail.getUser().getId());

            System.out.println("Token: " + token);
            return token;
        } catch (Exception e) {
            System.out.println("❌ Lỗi xác thực: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e;
        }
    }
    @Override
    public UserDTO getUser(Long id) {
        User user= userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        UserDTO userDTO=userMapper.toDTO(user);
        return userDTO;
    }


}
