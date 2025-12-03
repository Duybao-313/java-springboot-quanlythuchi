package com.duybao.QUANLYCHITIEU.Model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class CustomUserDetail implements UserDetails {

    private final User User;

    public CustomUserDetail(User User) {
        this.User = User;
    }

    public User getUser() {
        return User;
    }

    @Override
    public String getUsername() {
        return User.getUsername();
    }

    @Override
    public String getPassword() {
        return User.getPassword(); // đã mã hóa bằng BCrypt
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(User.getRole().getName()));
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return new ArrayList<>(user.getRoles()).stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//    }
