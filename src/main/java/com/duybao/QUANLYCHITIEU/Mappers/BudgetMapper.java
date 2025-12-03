package com.duybao.QUANLYCHITIEU.Mappers;

import com.duybao.QUANLYCHITIEU.Model.Budget;
import com.duybao.QUANLYCHITIEU.Model.Category;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Response.budget.BudgetResponse;
import com.duybao.QUANLYCHITIEU.Response.budget.request.BudgetRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    // Map từ Entity sang DTO
    @Mapping(source = "category.name", target = "categoryName")
    BudgetResponse toResponse(Budget budget);

    // Map từ Request sang Entity
    @Mapping(target = "id", ignore = true) // bỏ qua id khi tạo mới
    @Mapping(target = "user", source = "user")
    @Mapping(target = "category", source = "category")
    Budget toEntity(BudgetRequest request, User user, Category category);
}