package com.duybao.QUANLYCHITIEU.Service;


import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.AdminDashboardOverview;
import com.duybao.QUANLYCHITIEU.DTO.Response.Admin.UserSummaryDto;
import com.duybao.QUANLYCHITIEU.DTO.Response.User.UserDTO;
import com.duybao.QUANLYCHITIEU.Mappers.UserMapper;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.CategoryRepository;
import com.duybao.QUANLYCHITIEU.Repository.TransactionRepository;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;
import com.duybao.QUANLYCHITIEU.Repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final UserMapper userMapper;
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
}
