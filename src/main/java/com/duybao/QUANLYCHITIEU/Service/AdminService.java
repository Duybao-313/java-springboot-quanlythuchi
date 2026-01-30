package com.duybao.QUANLYCHITIEU.Service;


import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.AdminDashboardOverview;
import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.ImportResultDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.RowError;
import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.UserSummaryDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.category.CategoryResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.CategoryRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.admin.CategoryUpdateRequest;
import com.duybao.QUANLYCHITIEU.DTO.request.admin.UserUpdateRequest;
import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
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
import org.apache.poi.ss.usermodel.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
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
        user.getCategories().removeIf(c -> Objects.equals(c.getId(), id));
        userRepository.save(user);
        transactionRepository.deleteAllByCategoryId(id);
        categoryRepository.deleteById(id);

        }

    @Transactional
    public ImportResultDto importFromExcel(MultipartFile file) {
        List<RowError> errors = new ArrayList<>();
        List<Category> buffer = new ArrayList<>();
        final int BATCH = 500;

        int total = 0;
        int success = 0;

        try (InputStream in = file.getInputStream();
             Workbook wb = WorkbookFactory.create(in)) {

            Sheet sheet = wb.getSheetAt(0);
            int rowNum = 0;
            for (Row row : sheet) {
                rowNum++;
                if (rowNum == 1) continue; // bỏ qua header (dòng 1)

                total++;
                try {
                    // Lấy cố định theo cột: 0=A,1=B,2=C,3=D
                    String name = getCellValueAsString(row, 0);       // A
                    String type = getCellValueAsString(row, 1);       // B
                    String iconUrl = getCellValueAsString(row, 2);    // C

                    if (name == null || name.isBlank()) {
                        throw new IllegalArgumentException("name required");
                    }
                    if(type==null||type.isBlank()){
                        throw new IllegalArgumentException("type required");
                    }
                    if(categoryRepository.findByNameIgnoreCase(name).isPresent())
                    {
                        throw new AppException(ErrorCode.INVALID_REQUEST);
                    }


                    Category c = new Category();

                    c.setName(name.trim());
                    // nếu entity Category không có type/iconUrl/ownerId, bỏ hoặc map tương ứng
                    c.setType(TransactionType.valueOf(type.trim()));
                    c.setIconUrl(iconUrl == null ? null : iconUrl.trim());
                    c.setOwner(null);

                    buffer.add(c);

                    if (buffer.size() >= BATCH) {
                        categoryRepository.saveAll(buffer);
                        categoryRepository.flush();
                        buffer.clear();
                    }

                    success++;
                } catch (Exception ex) {
                    errors.add(new RowError(rowNum, ex.getMessage()));
                }
            }

            if (!buffer.isEmpty()) {
                categoryRepository.saveAll(buffer);
                categoryRepository.flush();
                buffer.clear();
            }

        } catch (Exception e) {
            errors.add(new RowError(-1, "File read error: " + e.getMessage()));
        }

        // success - errors.size() đảm bảo không âm; bạn có thể tính lại chính xác nếu muốn
        int successCount = Math.max(0, success - errors.size());
        return new ImportResultDto(total, successCount, errors);
    }

    /* Helper: đọc cell an toàn và trả String */
    private String getCellValueAsString(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    // tránh scientific notation
                    return BigDecimal.valueOf(cell.getNumericCellValue())
                            .stripTrailingZeros()
                            .toPlainString();
                }
            case FORMULA:
                FormulaEvaluator eval = row.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue v = eval.evaluate(cell);
                return switch (v.getCellType()) {
                    case STRING -> v.getStringValue();
                    case BOOLEAN -> Boolean.toString(v.getBooleanValue());
                    case NUMERIC -> BigDecimal.valueOf(v.getNumberValue()).stripTrailingZeros().toPlainString();
                    default -> null;
                };
            default:
                return null;
        }
    }

    private String getCell(Row r, int idx) {
        Cell c = r.getCell(idx);
        if (c == null) return null;
        c.setCellType(CellType.STRING);
        return c.getStringCellValue();
    }
    }


