package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.DTO.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.CategoryRequest;
import com.duybao.QUANLYCHITIEU.Service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CategoryResponse> createCategory(
            @RequestPart(name = "file", required = false) MultipartFile file,
            @RequestPart(name = "data") @Valid CategoryRequest request
    ) {
        CategoryResponse category = categoryService.createCategory(request, file);
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Tạo thành công danh mục")
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(category)
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getCategories(){
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
                                                        @RequestPart(name = "file", required = false) MultipartFile file,
                                                        @RequestPart(name = "data", required = false) String dataJson) throws JsonProcessingException  {
        if (dataJson == null || dataJson.isBlank()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        CategoryRequest request = new ObjectMapper().readValue(dataJson, CategoryRequest.class);
        return ApiResponse.<CategoryResponse>builder()
                .message("Cập nhật danh mục thành công")
                .data(categoryService.updateCategory(id, request,file))
                .success(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory( id);
        return ApiResponse.<Void>builder()
                .message("Xóa danh mục thành công")
                .success(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
}