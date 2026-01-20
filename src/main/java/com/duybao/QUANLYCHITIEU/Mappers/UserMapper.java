package com.duybao.QUANLYCHITIEU.Mappers;

import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.UserSummaryDto;
import com.duybao.QUANLYCHITIEU.DTO.request.UpdateUserRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.UserRegisterRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.Model.User;

import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", uses = {WalletMapper.class, TransactionMapper.class, CategoryMapper.class})
public interface UserMapper {
    UserDTO toDTO(User user);

    @Mapping(target = "walletCount", ignore = true)
    @Mapping(target = "id", source = "id")
    UserSummaryDto toAdminDTO(User user);

    List<UserSummaryDto> toAdminDTOs(List<User> users);

    List<UserDTO> toDTOs(List<User> users);
    User toEntity(UserRegisterRequest user);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateUserRequest userRequest, @MappingTarget User userStore);
}
