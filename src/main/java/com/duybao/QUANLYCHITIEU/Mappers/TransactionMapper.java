package com.duybao.QUANLYCHITIEU.Mappers;

import com.duybao.QUANLYCHITIEU.Model.Transaction;
import com.duybao.QUANLYCHITIEU.Response.Transaction.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "wallet.name", target = "walletName")
    TransactionResponse toDTO(Transaction transaction);

    List<TransactionResponse> toDTOs(List<Transaction> transactions);

}