package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.Response.category.Request.CategoryRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserCategoryService {
    public CategoryResponse assignExistingCategory(Long userId, Long categoryId);
    public void removeCategoryFromUser(Long userId, Long categoryId);
    public CategoryResponse createAndAssignCategory(Long userId, CategoryRequest req, MultipartFile file);
}
