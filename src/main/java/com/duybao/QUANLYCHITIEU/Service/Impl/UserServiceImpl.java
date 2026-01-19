package com.duybao.QUANLYCHITIEU.Service.Impl;

import com.duybao.QUANLYCHITIEU.Config.SecurityConfigV2;
import com.duybao.QUANLYCHITIEU.DTO.request.UpdateUserRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.UserMapper;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;
import com.duybao.QUANLYCHITIEU.Service.ImageService;
import com.duybao.QUANLYCHITIEU.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;
    private final UserMapper userMapper;
    private final SecurityConfigV2 securityConfig;
@PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserDTO> getAllUser() {
    return userMapper.toDTOs(userRepository.findAll());
    }

    @Override
    public UserDTO getUser(Long id) {
        var context= SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(context).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        UserDTO userDTO= userMapper.toDTO(user);
        userDTO.setRole(user.getRole().getName());
        return userDTO;
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserRequest userRequest) {
        User userStore = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.update(userRequest, userStore);
        userRepository.save(userStore);
        return userMapper.toDTO(userStore);
    }
    @Override
    public void setAvatar(MultipartFile file,Long id){
     String avt_url;
    try {
        avt_url = imageService.uploadImage(file, "QLCT_folder");
        } catch (AppException | IOException e) {
            throw new AppException(ErrorCode.READ_FILE_ERROR);
        }
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setAvatarUrl(avt_url);
        userRepository.save(user);
    };

}
