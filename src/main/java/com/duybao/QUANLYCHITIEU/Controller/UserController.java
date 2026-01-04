package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.Model.CustomUserDetail;
import com.duybao.QUANLYCHITIEU.Response.User.Request.UpdateUserRequest;
import com.duybao.QUANLYCHITIEU.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.Response.category.Request.CategoryRequest;
import com.duybao.QUANLYCHITIEU.Service.UserCategoryService;
import com.duybao.QUANLYCHITIEU.Service.UserService;
import com.duybao.QUANLYCHITIEU.Service.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private  final UserCategoryService userCategoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ApiResponse<List<UserDTO>> getAllUser() {
        List<UserDTO> users = userService.getAllUser();

        return ApiResponse.<List<UserDTO>>builder()
                .success(true)
                .message("Tất cả người dùng")
                .data(users)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/info")
    public ApiResponse<UserDTO> getCurrentUser(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        return ApiResponse.<UserDTO>builder()
                .data(userService.getUser(customUserDetail.getUser().getId()))
                .success(true)
                .message("Lấy thông tin người dùng")

                .timestamp(LocalDateTime.now())
                .build();
    }


    @PutMapping("/updateinfo")
    public ApiResponse<UserDTO> updateUser(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid UpdateUserRequest userRequest) {
        Long userId = userDetail.getUser().getId();;
        UserDTO kq= userService.updateUser(userId, userRequest);
        return ApiResponse.<UserDTO>builder()
                .success(true)
                .message("Cập nhật thông tin thành công")
                .data(kq)
                .timestamp(LocalDateTime.now())
                .build();
    }
//Category
    @GetMapping("/me/categories")
    public ApiResponse<List<CategoryResponse>>getCategories( @AuthenticationPrincipal CustomUserDetail userDetails){
        List<CategoryResponse> res;
        res=userCategoryService.getCategoriesBUser(userDetails.getUser().getId());
        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Tạo và gán danh mục thành công thành công")
                .data(res)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PostMapping("/categories")
    public ApiResponse<CategoryResponse> createCategoryForMe(
            @RequestPart(name = "file", required = false) MultipartFile file,
            @RequestPart(name = "data", required = true)  @Valid CategoryRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetails) {

        Long userId = userDetails.getUser().getId();
        CategoryResponse res = userCategoryService.createAndAssignCategory(userId, request, file);

        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Tạo và gán danh mục thành công")
                .data(res)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostMapping("/me/categories/{categoryId}")
    public ApiResponse<CategoryResponse> AssignCategoryForMe(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetail userDetails) {

        Long userId = userDetails.getUser().getId();
        CategoryResponse res = userCategoryService.assignExistingCategory(userId, categoryId);
        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Gán danh mục thành công thành công")
                .data(res)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @DeleteMapping("/me/categories/{categoryId}")
    public ApiResponse<Void> removeCategoryForMe(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal  CustomUserDetail userDetails) {

        Long userId = userDetails.getUser().getId();
        userCategoryService.removeCategoryFromUser(userId, categoryId);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Gỡ danh mục thành công thành công")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
