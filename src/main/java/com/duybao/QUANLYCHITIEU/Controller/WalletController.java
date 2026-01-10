package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.DTO.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.DTO.request.WalletRequest;
import com.duybao.QUANLYCHITIEU.DTO.Response.Wallet.WalletOverview;
import com.duybao.QUANLYCHITIEU.DTO.Response.Wallet.WalletResponse;
import com.duybao.QUANLYCHITIEU.Service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;


    @PostMapping
    public ApiResponse<WalletResponse> createWallet(  @RequestPart(name = "file", required = false) MultipartFile file,
                                                      @RequestPart(name = "data", required = true)  @Valid WalletRequest request,
                                                    @AuthenticationPrincipal User userDetails) {
        WalletResponse wallet = walletService.createWallet(userDetails.getId(), request,file);
        return ApiResponse.<WalletResponse>builder()
                .success(true)
                .code(200)
                .message("Tạo ví thành công")
                .data(wallet)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping
    public ApiResponse<List<WalletResponse>> getWallets(@AuthenticationPrincipal User userDetails) {
        List<WalletResponse> wallets = walletService.getWalletsByUser(userDetails.getId());
        return ApiResponse.<List<WalletResponse>>builder()
                .success(true)
                .code(200)
                .message("Danh sách ví")
                .data(wallets)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<WalletResponse> updateWallet(@PathVariable Long id,
                                                    @RequestBody WalletRequest request,
                                                    @AuthenticationPrincipal User userDetails) {
        return ApiResponse.<WalletResponse>builder()
                .message("Cập nhật ví thành công")
                .data(walletService.updateWallet(userDetails.getId(), id, request))
                .success(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteWallet(@PathVariable Long id,
                                          @AuthenticationPrincipal User userDetails) {
        walletService.deleteWallet(userDetails.getId(), id);
        return ApiResponse.<Void>builder()
                .message("Xóa ví thành công")
                .success(true)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @GetMapping("/overview")
    public ApiResponse<WalletOverview>overviewWallet(@AuthenticationPrincipal User userDetail,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        WalletOverview data=walletService.WalletOverview(userDetail.getId(),startDate,endDate);
        return ApiResponse.<WalletOverview>builder()
                .message("Thống kê của ví")
                .success(true)
                .code(200)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}