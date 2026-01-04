package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import com.duybao.QUANLYCHITIEU.Model.CustomUserDetail;
import com.duybao.QUANLYCHITIEU.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.Response.Transaction.Request.TransactionRequest;
import com.duybao.QUANLYCHITIEU.Response.Transaction.TransactionResponse;
import com.duybao.QUANLYCHITIEU.Service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ApiResponse<TransactionResponse> createTransaction(@RequestBody @Valid TransactionRequest request,
                                                      @AuthenticationPrincipal CustomUserDetail userDetails) {
        return ApiResponse.<TransactionResponse>builder()
                .message("Tạo thành công giao dịch")
                .data(transactionService.createTransaction(userDetails.getUser().getId(),request))
                .success(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @GetMapping("/user")
    public ApiResponse<List<TransactionResponse>> getForUser(
           @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long walletId,
           @RequestParam(required = false) TransactionType type,
           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var userId=userDetail.getUser().getId();
        var list = transactionService.getTransactionsByUser(userId,type, startDate, endDate, categoryId, walletId);
        return ApiResponse.<List<TransactionResponse>>builder()
                .message("Danh sách giao dịch")
                .data(list)
                .success(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<TransactionResponse> updateTransaction(@PathVariable Long id,
                                                              @RequestBody TransactionRequest request,
                                                              @AuthenticationPrincipal CustomUserDetail userDetails) {
        return ApiResponse.<TransactionResponse>builder()
                .message("Cập nhật giao dịch thành công")
                .data(transactionService.updateTransaction(userDetails.getUser().getId(), id, request))
                .success(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTransaction(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetail userDetails) {
        transactionService.deleteTransaction(userDetails.getUser().getId(), id);
        return ApiResponse.<Void>builder()
                .message("Xóa giao dịch thành công")
                .success(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
}