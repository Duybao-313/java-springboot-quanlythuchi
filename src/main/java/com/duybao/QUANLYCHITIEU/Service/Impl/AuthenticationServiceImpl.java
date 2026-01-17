package com.duybao.QUANLYCHITIEU.Service.Impl;


import com.duybao.QUANLYCHITIEU.DTO.request.ChangePasswordRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.LogoutRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.UserLoginRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.UserRegisterRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.Enum.UserStatus;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.UserMapper;
import com.duybao.QUANLYCHITIEU.Model.InvalidatedToken;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.InvalidatedTokenRepository;
import com.duybao.QUANLYCHITIEU.Repository.RoleRepository;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;

import com.duybao.QUANLYCHITIEU.DTO.Response.AuthResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.RegisterResponse;
import com.duybao.QUANLYCHITIEU.Service.AuthenticationService;
import com.duybao.QUANLYCHITIEU.Service.JwtService;


import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    @Value("${jwt.secret}")
    protected String SECRET_KEY ;

    private final UserMapper userMapper;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final InvalidatedTokenRepository invalidatedTokenRepository;


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

            String token = jwtService.generateToken(user1).getToken();
            UserDTO userDTO = userMapper.toDTO(user1); //
            String role = user1.getRole().getName();
            return new AuthResponse(token, role, userDTO);
        }
catch (BadCredentialsException e) {
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }
    }


    @Override
    public UserDTO getUser(Long id) {
        User user= userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
               return userMapper.toDTO(user);
    }
    @Override
    public void Logout(LogoutRequest request) throws ParseException, JOSEException {
      try  {
            var signToken = jwtService.VerifyToken(request.getToken(), true);
            String id = signToken.getJWTClaimsSet().getJWTID();
            Date dateExpiry = signToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(id)
                    .expiryTime(dateExpiry)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }catch (AppException e){
          log.info("token exp");
      }
    };
    public boolean changePassword(ChangePasswordRequest request, Long id){
      User  user =userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
      var oldPass =request.getOldPass();
        var newPass1 =request.getNewPass1();
        var newPass2 =request.getNewPass2();
        if( !passwordEncoder.matches(oldPass,user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        if (!newPass1.equals(newPass2))
            throw new AppException(ErrorCode.INVALID_REQUEST);
        if(newPass1.equals(oldPass))
            throw new AppException(ErrorCode.SAME_PASSWORD);
        user.setPassword(passwordEncoder.encode(newPass1));
        userRepository.save(user);
        return true;

    };






}
