package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.Response.category.Request.CategoryRequest;

import java.util.List;

public interface CategoryService {


    CategoryResponse createCategory(CategoryRequest req);
    CategoryResponse updateCategory(Long id, CategoryRequest req);
    void deleteCategory(Long id);
    CategoryResponse getCategory(Long id);
    List<CategoryResponse> listAll();
}
