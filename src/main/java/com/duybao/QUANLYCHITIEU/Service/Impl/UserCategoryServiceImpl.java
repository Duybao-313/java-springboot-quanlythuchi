package com.duybao.QUANLYCHITIEU.Service.Impl;

import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.CategoryMapper;
import com.duybao.QUANLYCHITIEU.Model.Category;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.CategoryRepository;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;
import com.duybao.QUANLYCHITIEU.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.Response.category.Request.CategoryRequest;
import com.duybao.QUANLYCHITIEU.Service.ImageService;
import com.duybao.QUANLYCHITIEU.Service.UserCategoryService;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCategoryServiceImpl implements UserCategoryService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ImageService imageService;



    @Transactional
    public List<CategoryResponse> getCategoriesBUser(Long userID){
       List<Category> kq = categoryRepository.findByOwnerIdOrOwnerIsNull(userID);
       List<CategoryResponse> kq1;
       kq1=kq.stream().map(categoryMapper::toDTO).toList();
        return kq1;

    }
    @Transactional
    public CategoryResponse createAndAssignCategory(Long userId, CategoryRequest req, MultipartFile file) {
        String rawName = Optional.ofNullable(req.getName())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST));

        // Normalize chỉ để tìm (không lưu)
        String normalized = req.normalizeName(rawName, true);
        System.out.println("Normalized name = " + normalized);

        var type = Optional.ofNullable(req.getType())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST));

        // 1. Tìm category theo tên đã normalize
        Optional<Category> opt = categoryRepository.findByNameIgnoreCase(normalized);
        if (opt.isPresent()) {
            // Nếu đã tồn tại category cùng tên (theo normalize) => ném lỗi theo yêu cầu
            throw new AppException(ErrorCode.CATEGORY_EXIST);
        }
User user=userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        // 2. Tạo category mới với tên **nguyên gốc từ req** (không lưu normalized)
        String icon;
        try{
            icon=imageService.uploadImage(file,"QLCT-image");
        } catch (Exception ex) {
            throw  new AppException(ErrorCode.READ_FILE_ERROR);
        }
        Category category = Category.builder()
                .name(rawName)      // lưu tên theo request, không lưu normalized
                .type(type)
                .iconUrl(icon)
                .color(req.getColor())
                .owner(user)
                .build();

        try {
            category = categoryRepository.save(category);
            categoryRepository.flush(); // ép lỗi unique nếu DB có constraint
        } catch (DataIntegrityViolationException | ConstraintViolationException ex) {
            // Nếu DB ném duplicate (race) thì chuyển thành lỗi nghiệp vụ CATEGORY_EXIST
            throw new AppException(ErrorCode.CATEGORY_EXIST);
        }

        Long catId = category.getId();
        boolean mappingExists = user.getCategories().stream()
                .anyMatch(c -> Objects.equals(c.getId(), catId));

        if (!mappingExists) {
            user.getCategories().add(category);
            if (category.getUsers() != null) {
                category.getUsers().add(user);
            }
            userRepository.save(user);
            userRepository.flush();
        }

        return categoryMapper.toDTO(category);
    }
    @Transactional
    public CategoryResponse assignExistingCategory(Long userId, Long categoryId) {
        // 1. Lấy category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // 2. Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 3. Kiểm tra mapping đã tồn tại (dùng collection trong entity để tránh query sai)
        Long catId = category.getId(); // effectively final
        boolean mappingExists = user.getCategories().stream()
                .anyMatch(c -> Objects.equals(c.getId(), catId));

        if (mappingExists) {
            // Nếu bạn muốn không ném lỗi mà trả về DTO, đổi logic ở đây
            throw new AppException(ErrorCode.CATEGORY_ALREADY_ASSIGNED);
        }

        // 4. Thêm mapping (cập nhật cả hai chiều nếu có)
        user.getCategories().add(category);
        if (category.getUsers() != null) {
            category.getUsers().add(user);
        }

        // 5. Lưu và flush để đảm bảo persist và bắt lỗi FK nếu có
        userRepository.save(user);
        userRepository.flush();

        // 6. Trả về DTO của category (hoặc user tuỳ yêu cầu)
        return categoryMapper.toDTO(category);
    }
    @Transactional
    public void removeCategoryFromUser(Long userId, Long categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean removed = user.getCategories().removeIf(c -> Objects.equals(c.getId(), categoryId));
        if (removed) {
            userRepository.save(user);
        }
    }
}