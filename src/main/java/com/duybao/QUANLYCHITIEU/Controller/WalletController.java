package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.Model.CustomUserDetail;
import com.duybao.QUANLYCHITIEU.Model.Wallet;
import com.duybao.QUANLYCHITIEU.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.Response.Wallet.Request.WalletRequest;
import com.duybao.QUANLYCHITIEU.Response.Wallet.WalletResponse;
import com.duybao.QUANLYCHITIEU.Service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ApiResponse<WalletResponse> createWallet(@RequestBody @Valid WalletRequest request,
                                                    @AuthenticationPrincipal CustomUserDetail userDetails) {
        WalletResponse wallet = walletService.createWallet(userDetails.getUser().getId(), request);
        return ApiResponse.<WalletResponse>builder()
                .success(true)
                .code("200")
                .message("Tạo ví thành công")
                .data(wallet)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping
    public ApiResponse<List<WalletResponse>> getWallets(@AuthenticationPrincipal CustomUserDetail userDetails) {
        List<WalletResponse> wallets = walletService.getWalletsByUser(userDetails.getUser().getId());
        return ApiResponse.<List<WalletResponse>>builder()
                .success(true)
                .code("200")
                .message("Danh sách ví")
                .data(wallets)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<WalletResponse> updateWallet(@PathVariable Long id,
                                                    @RequestBody WalletRequest request,
                                                    @AuthenticationPrincipal CustomUserDetail userDetails) {
        return ApiResponse.<WalletResponse>builder()
                .message("Cập nhật ví thành công")
                .data(walletService.updateWallet(userDetails.getUser().getId(), id, request))
                .success(true)
                .code("200")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteWallet(@PathVariable Long id,
                                          @AuthenticationPrincipal CustomUserDetail userDetails) {
        walletService.deleteWallet(userDetails.getUser().getId(), id);
        return ApiResponse.<Void>builder()
                .message("Xóa ví thành công")
                .success(true)
                .code("200")
                .timestamp(LocalDateTime.now())
                .build();
    }
}