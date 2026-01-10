package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.DTO.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.CategoryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {


    CategoryResponse createCategory(CategoryRequest req, MultipartFile file);
    CategoryResponse updateCategory(Long id, CategoryRequest req,MultipartFile file);
    void deleteCategory(Long id);
    CategoryResponse getCategory(Long id);
    List<CategoryResponse> listAll();
}
