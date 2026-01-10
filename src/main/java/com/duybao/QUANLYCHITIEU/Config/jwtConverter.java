package com.duybao.QUANLYCHITIEU.Config;

import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class jwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository; // hoặc UserService

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String userIdStr = jwt.getClaimAsString("userid"); // claim bạn lưu khi tạo token
        Collection<GrantedAuthority> authorities = new JwtGrantedAuthoritiesConverter().convert(jwt);

        if (userIdStr == null) {
            return new JwtAuthenticationToken(jwt, authorities);
        }

        Long userId = Long.valueOf(userIdStr);
        // Load user từ DB; nếu User implements UserDetails thì trả thẳng
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new JwtAuthenticationToken(jwt, authorities);
        }

        // principal là User (implements UserDetails)
        return new UsernamePasswordAuthenticationToken(user, jwt, authorities);
    }
}