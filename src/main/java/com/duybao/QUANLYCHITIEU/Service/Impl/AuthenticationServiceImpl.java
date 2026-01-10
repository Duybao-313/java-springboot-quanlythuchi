package com.duybao.QUANLYCHITIEU.Service.Impl;


import com.duybao.QUANLYCHITIEU.DTO.request.UserLoginRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.UserRegisterRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.Enum.UserStatus;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.UserMapper;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.RoleRepository;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;

import com.duybao.QUANLYCHITIEU.DTO.Response.AuthResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.RegisterResponse;
import com.duybao.QUANLYCHITIEU.Service.AuthenticationService;
import com.duybao.QUANLYCHITIEU.Service.JwtService;



import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return  RegisterResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build();


    }



    @Override
    public AuthResponse login(UserLoginRequest a) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(a.getUsername(),a.getPassword()
            );
            Authentication authentication= authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user1 =(User) authentication.getPrincipal();

            String token = jwtService.generateToken(user1);
            UserDTO userDTO = userMapper.toDTO(user1); //
            String role = user1.getRole().getName();
            return new AuthResponse(token, role, userDTO);
        }
catch (BadCredentialsException e) {
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }
    }

    @Override
    public String jwtcode(UserLoginRequest a) {
        try {
           Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(a.getUsername(), a.getPassword())
            );
            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user);

            System.out.println("Token: " + token);
            return token;
        } catch (Exception e) {
            System.out.println(" Lỗi xác thực: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e;
        }
    }
    @Override
    public UserDTO getUser(Long id) {
        User user= userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDTO(user);
    }


}
