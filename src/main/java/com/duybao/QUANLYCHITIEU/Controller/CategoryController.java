package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.Model.CustomUserDetail;
import com.duybao.QUANLYCHITIEU.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.Response.category.Request.CategoryRequest;
import com.duybao.QUANLYCHITIEU.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request
                                                      ) {
        CategoryResponse category = categoryService.createCategory( request);
        return ApiResponse.<CategoryResponse>builder()
                .code("200")
                .message("Tạo thành công danh mục")
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(category)
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getCategories(@AuthenticationPrincipal CustomUserDetail userDetails) {
        List<CategoryResponse> categories = categoryService.listAll();
        return ApiResponse.<List<CategoryResponse>>builder()
                .message("Lấy thành công danh sách danh mục")
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(categories)
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable Long id,
                                                        @RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .message("Cập nhật danh mục thành công")
                .data(categoryService.updateCategory(id, request))
                .success(true)
                .code("200")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory( id);
        return ApiResponse.<Void>builder()
                .message("Xóa danh mục thành công")
                .success(true)
                .code("200")
                .timestamp(LocalDateTime.now())
                .build();
    }
}