package com.duybao.QUANLYCHITIEU.Mappers;

import com.duybao.QUANLYCHITIEU.Model.Wallet;
import com.duybao.QUANLYCHITIEU.Response.Wallet.WalletResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletResponse toDTO(Wallet wallet);
    List<WalletResponse> toDTOs(List<Wallet> wallets);

}
