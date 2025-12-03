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
import com.duybao.QUANLYCHITIEU.Service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;


    @Transactional
    public CategoryResponse createCategory(CategoryRequest req) {
        String name = Optional.ofNullable(req.getName()).map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST));




        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new AppException(ErrorCode.CATEGORY_EXIST);
        }

        try {
            Category c = Category.builder()
                    .name(name)
                    .type(req.getType())
                    .iconUrl(req.getIconUrl())
                    .color(req.getColor())
                    .build();
            c = categoryRepository.save(c);
            return categoryMapper.toDTO(c);
        } catch (DataIntegrityViolationException ex) {
            throw new AppException(ErrorCode.CATEGORY_EXIST);
        }
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest req) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        String newName = Optional.ofNullable(req.getName()).map(String::trim)
                .filter(s -> !s.isEmpty()).orElse(c.getName());

        if (!c.getName().equalsIgnoreCase(newName) && categoryRepository.existsByNameIgnoreCase(newName)) {
            throw new AppException(ErrorCode.CATEGORY_EXIST);
        }

        c.setName(newName);
        c.setType(req.getType());
        c.setIconUrl(req.getIconUrl());
        c.setColor(req.getColor());


        try {
            Category updated = categoryRepository.save(c);
            return categoryMapper.toDTO(updated);
        } catch (DataIntegrityViolationException ex) {
            throw new AppException(ErrorCode.CATEGORY_EXIST);
        }
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        // Nếu cần kiểm tra ràng buộc business (ví dụ đang dùng bởi user) thì kiểm tra ở đây
        categoryRepository.delete(c);
    }

    public CategoryResponse getCategory(Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toDTO(c);
    }

    public List<CategoryResponse> listAll() {
        return categoryRepository.findAll().stream().map((c)->categoryMapper.toDTO(c)).collect(Collectors.toList());
    }


}