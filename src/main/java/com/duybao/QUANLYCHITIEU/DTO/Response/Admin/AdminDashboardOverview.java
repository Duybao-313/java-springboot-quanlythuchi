package com.duybao.QUANLYCHITIEU.DTO.Response.Admin;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class AdminDashboardOverview {

    // Overview
    private Long totalUsers;
    private Long activeUsersToday;
    private Long totalWallets;
    private Long totalCategoriesByAdmin;
    private BigDecimal totalSystemBalance;
//    private BigDecimal totalOverdraft;
//    private BigDecimal feesCollectedToday;
    private LocalDateTime generatedAt;

    // Transactions summary
    private Long transactionsToday;
//    private BigDecimal transactionsLast24hVolume;


    // Risk
//    private Integer openFraudAlerts;
//    private Integer highRiskUsers;
//    private Integer avgFraudScore;

    // Operations
//    private Integer pendingKycCount;
//    private Integer pendingReversals;
//    private Integer reconciliationMismatches;


//    // Recent lists
//    private List<TransactionSummaryDto> recentTransactions;
    private List<UserSummaryDto> recentUsers;
//    private List<AuditDto> recentAdminActions;
//
//    // UI helpers
//    private List<ActionLinkDto> quickLinks;
//    private List<String> permissions;
}