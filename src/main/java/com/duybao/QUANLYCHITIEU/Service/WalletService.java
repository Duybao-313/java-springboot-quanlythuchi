package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.Response.Wallet.Request.WalletRequest;
import com.duybao.QUANLYCHITIEU.Response.Wallet.WalletOverview;
import com.duybao.QUANLYCHITIEU.Response.Wallet.WalletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WalletService {
    public WalletResponse createWallet(Long id, WalletRequest request, MultipartFile file);
   public List<WalletResponse> getWalletsByUser(Long id);
    public WalletResponse updateWallet(Long userId, Long id, WalletRequest request) ;

    public void deleteWallet(Long userId, Long id) ;
    public WalletOverview WalletOverview(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
