package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.DTO.request.UpdateUserRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.DTO.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.CategoryRequest;
import com.duybao.QUANLYCHITIEU.Service.UserCategoryService;
import com.duybao.QUANLYCHITIEU.Service.UserService;
import com.duybao.QUANLYCHITIEU.Service.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
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

        @PostMapping(path = "/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  ApiResponse<?>uploadAVT(@AuthenticationPrincipal User user,
                                    @RequestPart(name = "file", required = false) MultipartFile file){
        userService.setAvatar(file,user.getId());
        return ApiResponse.builder()
                .success(true)
                .message("tai anh thanh cong")
                .build();

        }

    @GetMapping("/info")
    public ApiResponse<UserDTO> getCurrentUser(@AuthenticationPrincipal User user) {


        return ApiResponse.<UserDTO>builder()
                .data(userService.getUser( user.getId()))
                .success(true)
                .message("Lấy thông tin người dùng")

                .timestamp(LocalDateTime.now())
                .build();
    }


    @PutMapping("/updateinfo")
    public ApiResponse<UserDTO> updateUser(@AuthenticationPrincipal User userDetail, @RequestBody @Valid UpdateUserRequest userRequest) {
        Long userId = userDetail.getId();;
        UserDTO kq= userService.updateUser(userId, userRequest);
        return ApiResponse.<UserDTO>builder()
                .success(true)
                .message("Cập nhật thông tin thành công")
                .data(kq)
                .timestamp(LocalDateTime.now())
                .build();
    }
//Category
    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>>getCategories( @AuthenticationPrincipal User userDetails){
        List<CategoryResponse> res;
        res=userCategoryService.getCategoriesBUser(userDetails.getId());
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
            @RequestPart(name = "data", required = false)  @Valid CategoryRequest request,
            @AuthenticationPrincipal User userDetails) throws IOException {

        Long userId = userDetails.getId();
        CategoryResponse res = userCategoryService.createAndAssignCategory(userId, request, file);

        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Tạo và gán danh mục thành công")
                .data(res)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostMapping("/categories/{categoryId}")
    public ApiResponse<CategoryResponse> AssignCategoryForMe(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal User userDetails) {

        Long userId = userDetails.getId();
        CategoryResponse res = userCategoryService.assignExistingCategory(userId, categoryId);
        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Gán danh mục thành công thành công")
                .data(res)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @DeleteMapping("/categories/{categoryId}")
    public ApiResponse<Void> removeCategoryForMe(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal User userDetails) {

        Long userId = userDetails.getId();
        userCategoryService.removeCategoryFromUser(userId, categoryId);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Gỡ danh mục thành công thành công")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
