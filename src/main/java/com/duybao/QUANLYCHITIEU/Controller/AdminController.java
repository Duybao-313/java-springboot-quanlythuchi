package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.AdminDashboardOverview;
import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.UserSummaryDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.Service.AdminService;
import com.duybao.QUANLYCHITIEU.Service.UserCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
   private final AdminService adminService;
    @GetMapping("/overview")
    public ApiResponse<AdminDashboardOverview> DashboardAdmin(){

        return ApiResponse.<AdminDashboardOverview>builder()
                .success(true)
                .data(adminService.getDashboardOverview())
                .message("admin dashboard")
                .build();
    }
    @GetMapping("/users")
    public ApiResponse<List<UserSummaryDto>> AllUsers(){

        return ApiResponse.<List<UserSummaryDto>>builder()
                .success(true)
                .data(adminService.getUsers())
                .message("List User")
                .build();
    }
}
