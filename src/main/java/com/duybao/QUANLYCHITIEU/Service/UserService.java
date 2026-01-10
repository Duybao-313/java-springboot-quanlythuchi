package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.DTO.request.UpdateUserRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.User.UserDTO;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface UserService {
    public List<UserDTO> getAllUser();


    public UserDTO getUser(Long id);

    public UserDTO updateUser(Long id, UpdateUserRequest userRequest);
    public void setAvatar(MultipartFile file,Long id);
}
