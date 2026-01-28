package com.duybao.QUANLYCHITIEU.Mappers;

import com.duybao.QUANLYCHITIEU.Model.Budget;
import com.duybao.QUANLYCHITIEU.Model.Category;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.BudgetRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

//    // Map từ Entity sang DTO
//    BudgetResponse toResponse(Budget budget);
//
//
//    Budget toEntity(BudgetRequest request, User user, Category category);
}