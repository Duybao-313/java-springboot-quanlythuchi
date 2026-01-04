package com.duybao.QUANLYCHITIEU.Config;

import com.duybao.QUANLYCHITIEU.Service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailService; // CustomUserDetailsService
    private final AuthenticationEntryPoint authenticationEntryPoint; // inject entry point

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails;
                try {
                    userDetails = this.userDetailService.loadUserByUsername(username);
                } catch (Exception e) {
                    // user not found or other error -> treat as authentication failure
                    logger.debug("UserDetails load failed for {}: {}", username, e.getMessage());
                    authenticationEntryPoint.commence(request, response,
                            new InsufficientAuthenticationException("User not found", e));
                    return;
                }

                // kiểm tra token hợp lệ so với userDetails (dùng normalized username nếu cần)
                if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // token không hợp lệ -> delegate cho entry point
                    authenticationEntryPoint.commence(request, response,
                            new InsufficientAuthenticationException("Token invalid"));
                    return;
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            logger.debug("JWT expired: {}", ex.getMessage());
            authenticationEntryPoint.commence(request, response,
                    new InsufficientAuthenticationException("Token expired", ex));
            return;
        } catch (JwtException ex) {
            logger.debug("JWT invalid: {}", ex.getMessage());
            authenticationEntryPoint.commence(request, response,
                    new InsufficientAuthenticationException("Token invalid", ex));
            return;
        } catch (Exception ex) {
            logger.error("Authentication filter error", ex);
            authenticationEntryPoint.commence(request, response,
                    new InsufficientAuthenticationException("Authentication error", ex));
            return;
        }
    }
}