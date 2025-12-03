package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Model.Wallet;
import com.duybao.QUANLYCHITIEU.Response.Wallet.Request.WalletRequest;
import com.duybao.QUANLYCHITIEU.Response.Wallet.WalletResponse;

import java.util.List;

public interface WalletService {
    public WalletResponse createWallet(Long id, WalletRequest request);
   public List<WalletResponse> getWalletsByUser(Long id);
    public WalletResponse updateWallet(Long userId, Long id, WalletRequest request) ;

    public void deleteWallet(Long userId, Long id) ;
}
