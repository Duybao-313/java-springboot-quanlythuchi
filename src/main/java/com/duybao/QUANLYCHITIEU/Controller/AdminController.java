package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.AdminDashboardOverview;
import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.ImportResultDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.UserSummaryDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.CategoryRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.admin.CategoryUpdateRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.admin.UserUpdateRequest;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@Validated
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/overview")
    public ApiResponse<AdminDashboardOverview> DashboardAdmin() {

        return ApiResponse.<AdminDashboardOverview>builder()
                .success(true)
                .data(adminService.getDashboardOverview())
                .message("admin dashboard")
                .build();
    }

    @GetMapping("/users")
    public ApiResponse<List<UserSummaryDto>> AllUsers() {

        return ApiResponse.<List<UserSummaryDto>>builder()
                .success(true)
                .data(adminService.getUsers())
                .message("Danh sách người dùng")
                .build();
    }

    @PostMapping("/update-user")
    public ApiResponse<Void> updateInfo(@RequestBody @Valid UserUpdateRequest req) {
        adminService.UpdateUser(req);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Cập nhật thành công")
                .build();
    }

    @GetMapping("/categories-admin")
    public ApiResponse<List<CategoryResponse>> ListCategoriesByAdmin() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .data(adminService.getCategoriesByAdmin())
                .message("Lấy danh mục thành công")
                .build();
    }

    @PostMapping(value = "/update-global-category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> updateCategory(
            @RequestPart(name = "file", required = false) MultipartFile file,
            @RequestPart(name = "data") @Valid CategoryUpdateRequest request) throws IOException {
        adminService.UpdateCate(request, file);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Cập nhật thành công")
                .build();

    }
    @Transactional
    @DeleteMapping("/delete/category")
    public ApiResponse<Void> deleteCategory(@AuthenticationPrincipal User user, @RequestParam Long id) {
        adminService.deleteCategoryByAdmin(user.getId(), id);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Xóa danh mục thành công")
                .build();


    }
    @PostMapping(value = "/import-categories", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ImportResultDto> importCategories(
            @RequestPart("file") MultipartFile file) {
        ImportResultDto result = adminService.importFromExcel(file);
        return ApiResponse.<ImportResultDto>builder()
                .success(true)
                .data(result)
                .build();

    }
}
