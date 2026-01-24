package com.duybao.QUANLYCHITIEU.Service;


import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.AdminDashboardOverview;
import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.UserSummaryDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.CategoryRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.admin.CategoryUpdateRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.admin.UserUpdateRequest;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.CategoryMapper;
import com.duybao.QUANLYCHITIEU.Mappers.UserMapper;
import com.duybao.QUANLYCHITIEU.Model.Category;
import com.duybao.QUANLYCHITIEU.Model.Role;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final ImageService imageService;

    public AdminDashboardOverview getDashboardOverview(){
        Long totalUser= (long) userRepository.findAll().size();
        Long totalWallets= (long) walletRepository.findAll().size();
        BigDecimal balanceSystem=walletRepository.SumBalanceAllUser();
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        Long transactionToday=transactionRepository.countTransactionByDate(start, end);
        Long activeUsersToday=userRepository.countByDate(start,end);

        List<UserSummaryDto> userSummaryDto=userMapper.toAdminDTOs(userRepository.findTop10ByOrderByCreatedAtDesc());
       Long totalCategoriesByAdmin=(long) categoryRepository.findByOwnerIdOrOwnerIsNull(null).size();
        return AdminDashboardOverview.builder()
                .totalUsers(totalUser)
                .totalWallets(totalWallets)
                .totalSystemBalance(balanceSystem)
                .transactionsToday(transactionToday)
                .generatedAt(LocalDateTime.now())
                .activeUsersToday(activeUsersToday)
                .recentUsers(userSummaryDto)
                .totalCategoriesByAdmin(totalCategoriesByAdmin)
                .build();
    }

    public List<UserSummaryDto> getUsers() {
        List<UserSummaryDto> res=userMapper.toAdminDTOs(userRepository.findAll());
        res.forEach(c -> c.setWalletCount(
                walletRepository.countWalletsByUserId(c.getId())
        ));

        return res ;
    }
    public void UpdateUser(UserUpdateRequest req){

            User user = userRepository.findById(req.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            Role role = roleRepository.findByName(req.getRole()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            user.setFullName(req.getFullName());
            user.setEmail(req.getEmail());
            user.setStatus(req.getStatus());
            user.setRole(role);
            user.setUpdatedAt(LocalDateTime.now());
            if (userRepository.existsByEmailAndIdNot(req.getEmail(), req.getId())) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }

            userRepository.save(user);
    };
    public List<CategoryResponse> getCategoriesByAdmin(){
         List<Category> res=categoryRepository.findByOwnerIdOrOwnerIsNull(null);
        List<CategoryResponse> categoryResponseList;
        categoryResponseList=res.stream().map(categoryMapper::toDTO).toList();
        return categoryResponseList;

    }
    public void UpdateCate(CategoryUpdateRequest req, MultipartFile file) throws IOException {

            Category category=categoryRepository.findById(req.getId()).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        String icon = category.getIconUrl();
        if(file!=null){
            icon=imageService.uploadImage(file,"QLCT-image");
        }
            category.setName(req.getName());
            category.setType(req.getType());
            category.setIconUrl(icon);
            categoryRepository.save(category);
    }

    public void deleteCategoryByAdmin(Long userId,Long id){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        categoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Category category =categoryRepository.findByOwnerIdAndId(userId,id);
        user.getCategories().removeIf(c -> Objects.equals(c.getId(), id));
        userRepository.save(user);
        transactionRepository.deleteAllByCategoryId(id);
        categoryRepository.delete(category);

        }


    }

