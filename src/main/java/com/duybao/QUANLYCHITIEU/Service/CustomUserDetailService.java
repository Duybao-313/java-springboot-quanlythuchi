package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Model.CustomUserDetail;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public CustomUserDetail loadUserByUsername(String username) {
        try {
            User User = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            return new CustomUserDetail(
                    User
            );
        } catch (Exception e) {
            System.out.println("❌ Lỗi trong loadUserByUsername: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e;
        }

    }


}
