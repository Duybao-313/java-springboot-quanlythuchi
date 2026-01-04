package com.duybao.QUANLYCHITIEU.Service.Impl;

import com.duybao.QUANLYCHITIEU.Config.SecurityConfig;
import com.duybao.QUANLYCHITIEU.Response.User.Request.UpdateUserRequest;
import com.duybao.QUANLYCHITIEU.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.UserMapper;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;
import com.duybao.QUANLYCHITIEU.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityConfig securityConfig;
@PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserDTO> getAllUser() {
        List<UserDTO> users = userMapper.toDTOs(userRepository.findAll());
        return users;
    }

    @Override
    public UserDTO getUser(Long id) {
        var context= SecurityContextHolder.getContext().getAuthentication().getName();
        User usera=userRepository.findByUsername(context).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDTO(usera);
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserRequest userRequest) {
        User userStore = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.update(userRequest, userStore);
        userRepository.save(userStore);
        UserDTO userUpdate=userMapper.toDTO(userStore);
        return userUpdate;
    }
}
