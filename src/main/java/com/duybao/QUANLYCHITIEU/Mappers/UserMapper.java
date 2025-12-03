package com.duybao.QUANLYCHITIEU.Mappers;

import com.duybao.QUANLYCHITIEU.Response.User.Request.UpdateUserRequest;
import com.duybao.QUANLYCHITIEU.Response.User.Request.UserRegisterRequest;
import com.duybao.QUANLYCHITIEU.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.Model.User;

import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", uses = {WalletMapper.class, TransactionMapper.class, CategoryMapper.class})
public interface UserMapper {
//    @Mapping(source = "wallets", target = "wallets")
//    @Mapping(source = "transactions", target = "transactions")
//    @Mapping(source = "categories", target = "categories")
    UserDTO toDTO(User user);

    List<UserDTO> toDTOs(List<User> users);
    User toEntity(UserRegisterRequest user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateUserRequest userRequest, @MappingTarget User userStore);
}
