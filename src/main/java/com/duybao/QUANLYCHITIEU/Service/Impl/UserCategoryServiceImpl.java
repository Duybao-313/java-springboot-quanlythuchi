package com.duybao.QUANLYCHITIEU.Service.Impl;

import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Mappers.CategoryMapper;
import com.duybao.QUANLYCHITIEU.Model.Category;
import com.duybao.QUANLYCHITIEU.Model.Transaction;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.CategoryRepository;
import com.duybao.QUANLYCHITIEU.Repository.TransactionRepository;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;
import com.duybao.QUANLYCHITIEU.DTO.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.CategoryRequest;
import com.duybao.QUANLYCHITIEU.Service.ImageService;
import com.duybao.QUANLYCHITIEU.Service.UserCategoryService;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCategoryServiceImpl implements UserCategoryService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private  final TransactionRepository transactionRepository;
    private final ImageService imageService;



    @Transactional
    public List<CategoryResponse> getCategoriesBUser(Long userID){
       List<Category> kq = categoryRepository.findByOwnerIdOrOwnerIsNull(userID);
       List<CategoryResponse> kq1;
       kq1=kq.stream().map(categoryMapper::toDTO).toList();
        return kq1;

    }
    @Transactional
    public CategoryResponse createAndAssignCategory(Long userId, CategoryRequest req, MultipartFile file) throws IOException {

        String rawName = Optional.ofNullable(req.getName())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST));
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
        String icon=null;
        if(file!=null) {
            icon = imageService.uploadImage(file, "QLCT-image");
        }
        Category category = Category.builder()
                .name(rawName)
                .type(type)
                .iconUrl(icon)
                .color(req.getColor())
                .owner(user)
                .build();

        try {
            category = categoryRepository.save(category);
            categoryRepository.flush(); // ép lỗi unique nếu DB có constraint
        } catch (DataIntegrityViolationException | ConstraintViolationException ex) {
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
       Category category =categoryRepository.findByOwnerIdAndId(userId,categoryId);
        user.getCategories().removeIf(c -> Objects.equals(c.getId(), categoryId));
        userRepository.save(user);
        List<Transaction> kq =transactionRepository.deleteAllByCategoryId(categoryId);

            categoryRepository.deleteById(categoryId);
    }
}