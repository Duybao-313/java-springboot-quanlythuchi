package com.duybao.QUANLYCHITIEU.Mappers;

import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetDto;
import com.duybao.QUANLYCHITIEU.DTO.request.ScopeDto;
import com.duybao.QUANLYCHITIEU.DTO.request.ThresholdDto;
import com.duybao.QUANLYCHITIEU.Model.*;
import com.duybao.QUANLYCHITIEU.DTO.Response.budget.BudgetResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.BudgetRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetMapper {


        public BudgetDto toDto(Budget b) ;
        public ThresholdDto toThresholdDto(BudgetThreshold b) ;
        public ScopeDto toScopeDto(BudgetScope b) ;

}