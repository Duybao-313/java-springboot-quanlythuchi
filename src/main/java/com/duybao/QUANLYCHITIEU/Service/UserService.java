package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.Response.User.Request.UpdateUserRequest;
import com.duybao.QUANLYCHITIEU.Response.User.UserDTO;


import java.util.List;

public interface UserService {
    public List<UserDTO> getAllUser();


    public UserDTO getUser(Long id);

    public UserDTO updateUser(Long id, UpdateUserRequest userRequest);
}
